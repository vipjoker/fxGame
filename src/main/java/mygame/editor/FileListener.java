package mygame.editor;

import java.io.File;
import java.nio.file.*;
import java.util.stream.Collectors;

public class FileListener {
    public static void main(String[] args) {
        ClassLoader classLoader = FileListener.class.getClassLoader();
        File file = new File(classLoader.getResource("scripts/").getFile());
        System.out.println(file.length());



        try (final WatchService watchService = FileSystems.getDefault().newWatchService()) {
            final WatchKey watchKey = file.toPath().register(watchService, StandardWatchEventKinds.ENTRY_MODIFY,
                    StandardWatchEventKinds.ENTRY_CREATE,
                    StandardWatchEventKinds.ENTRY_DELETE);


            WatchKey key;
            while ((key = watchService.take()) != null) {
                for (WatchEvent<?> event : key.pollEvents()) {
                    System.out.println(
                            "Event kind:" + event.kind()
                                    + ". File affected: " + event.context() + ".");
                    if(event.context() instanceof Path){
                        System.out.println(Files.readAllLines((Path) event.context()).stream().collect(Collectors.joining()));
                    }
                }
                key.reset();
            }


        }catch (Exception e){
            e.printStackTrace();
        }

    }


    private void listen() throws  Exception{
        WatchService watchService
                = FileSystems.getDefault().newWatchService();

        Path path = Paths.get(System.getProperty("user.home"));

        path.register(
                watchService,
                StandardWatchEventKinds.ENTRY_CREATE,
                StandardWatchEventKinds.ENTRY_DELETE,
                StandardWatchEventKinds.ENTRY_MODIFY);

        WatchKey key;
        while ((key = watchService.take()) != null) {
            for (WatchEvent<?> event : key.pollEvents()) {
                System.out.println(
                        "Event kind:" + event.kind()
                                + ". File affected: " + event.context() + ".");
            }
            key.reset();
        }
    }
}
