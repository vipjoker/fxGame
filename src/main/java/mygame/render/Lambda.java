package mygame.render;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;

/**
 * Created by Makhobey Oleh on 6/9/16.
 * email: tajcig@ya.ru
 */
public class Lambda {

    public void start() {


        int coouner;
        List<User> list = new ArrayList<>();

        list.add(new User("Oleh", 10, 10));
        list.add(new User("Ivan", 11, 10));
        list.add(new User("Petro", 18, 10));
        list.add(new User("Mikola", 20, 10));
        list.add(new User("Oleh", 7, 10));
        list.add(new User("Ziaba", 30, 10));
        list.add(new User("Ivan", 30, 10));
        list.add(new User("Oleh", 13, 10));


        System.out.println(

                list.stream().collect(

                        StringBuilder::new,
                        StringBuilder::append,
                        new BiConsumer<StringBuilder, StringBuilder>() {
                            @Override
                            public void accept(StringBuilder s, StringBuilder s2) {
                                System.out.println("Hello");
                            }
                        }
                )

        );
    }

//        String reduce = Arrays.asList(3, 2, 34, 12, 351)
//
//                .stream()
//                .sorted((l, r) -> r.compareTo(l))
//                .map(String::valueOf)
//                .reduce("String ", new BinaryOperator<String>() {
//                    @Override
//                    public String apply(String s, String s2) {
//                        return s + s2;
//                    }
//                });
//
//
//      String str = Stream.of("The snow glows white on   the mountain tonight")
//                .map(w -> w.split("\\s+")).flatMap(Arrays::stream)
//                .toString();
//


//
//        Map<Integer, String> collect = list.stream()
//                .reduce(new BinaryOperator<User>() {
//                    @Override
//                    public User apply(User user, User user2) {
//                        return null;
//                    }
//                })
//                .collect(Collectors.toMap(user -> user.money, user -> ""));
//
//
//
//
//
//        System.out.println(collect);
        // List<String> asList = stringStream.collect(Collectors.toList());

//            Map<String, List<Person>> peopleByCity
//                = personStream.collect(Collectors.groupingBy(Person::getCity));
//
//            Map<String, Map<String, List<Person>>> peopleByStateAndCity
//                = personStream.collect(Collectors.groupingBy(Person::getState,
//                                                                  Collectors.groupingBy(Person::getCity)));
//
//


        public static class User {
            int age;
            int money;
            String name;

            public User(String name, int age, int money) {
                this.name = name;
                this.age = age;
                this.money = money;
            }


            public Integer getAge() {
                return age;
            }

            public Integer getMoney() {
                return money;
            }

            public String getName() {
                return name;
            }

        }
    }