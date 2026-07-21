package newStuff.canvas;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.*;
import java.time.Instant;
import java.util.UUID;

    public class TokenStore {

        private static final String DB_DIR  = System.getProperty("user.home") + File.separator + "requests-app";
        private static final String DB_FILE = DB_DIR + File.separator + "clientFx.db";
        private static final String DB_URL  = "jdbc:sqlite:" + DB_FILE;



        public static void main(String[] args) throws Exception {

            initDatabase();

            String token = UUID.randomUUID().toString();
            long ttlSeconds = 3600; // 1 hour
            createToken(token, ttlSeconds);

            boolean valid = isTokenValid(token);
            System.out.println("Token valid? " + valid);

            int deleted = deleteExpiredTokens();
            System.out.println("Expired tokens deleted: " + deleted);
        }

        public static void initDatabase() throws Exception {
            Files.createDirectories(Path.of(DB_DIR));
            Class<?> aClass = Class.forName("org.sqlite.JDBC");
            try (Connection conn = DriverManager.getConnection(DB_URL);
                 Statement st = conn.createStatement()) {

                // Table for token storage and lifecycle timestamps
                st.execute("""
                CREATE TABLE IF NOT EXISTS tokens (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    token TEXT NOT NULL UNIQUE,
                    issued_at INTEGER NOT NULL,
                    expires_at INTEGER NOT NULL
                )
            """);

                st.execute("CREATE INDEX IF NOT EXISTS idx_tokens_expires_at ON tokens(expires_at)");
            }
        }

        public static void createToken(String token, long ttlSeconds) throws SQLException {
            long now = Instant.now().getEpochSecond();
            long expiresAt = now + ttlSeconds;

            String sql = "INSERT INTO tokens(token, issued_at, expires_at) VALUES(?, ?, ?)";

            try (Connection conn = DriverManager.getConnection(DB_URL);
                 PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, token);
                ps.setLong(2, now);
                ps.setLong(3, expiresAt);
                ps.executeUpdate();
            }
        }

        public static boolean isTokenValid(String token) throws SQLException {
            long now = Instant.now().getEpochSecond();
            String sql = "SELECT 1 FROM tokens WHERE token = ? AND expires_at > ? LIMIT 1";

            try (Connection conn = DriverManager.getConnection(DB_URL);
                 PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, token);
                ps.setLong(2, now);

                try (ResultSet rs = ps.executeQuery()) {
                    return rs.next();
                }
            }
        }

        public static int deleteExpiredTokens() throws SQLException {
            long now = Instant.now().getEpochSecond();
            String sql = "DELETE FROM tokens WHERE expires_at <= ?";

            try (Connection conn = DriverManager.getConnection(DB_URL);
                 PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setLong(1, now);
                return ps.executeUpdate();
            }
        }
    }


