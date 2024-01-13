import com.learn.Department;
import com.learn.Employee;
import com.learn.Event;
import com.learn.Position;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.*;

public class Streams {

    private List<Employee> emps = List.of(
            new Employee("Michael", "Smith", 243, 43, Position.CHEF),
            new Employee("Jane", "Smith", 523, 40, Position.MANAGER),
            new Employee("Jury", "Gagarin", 6423, 26, Position.MANAGER),
            new Employee("Jack", "London", 5543, 53, Position.WORKER),
            new Employee("Eric", "Jackson", 2534, 22, Position.WORKER),
            new Employee("Andrew", "Bosh", 3456, 44, Position.WORKER),
            new Employee("Joe", "Smith", 723, 30, Position.MANAGER),
            new Employee("Jack", "Gagarin", 7423, 35, Position.MANAGER),
            new Employee("Jane", "London", 7543, 42, Position.WORKER),
            new Employee("Mike", "Jackson", 7534, 31, Position.WORKER),
            new Employee("Jack", "Bosh", 7456, 54, Position.WORKER),
            new Employee("Mark", "Smith", 123, 41, Position.MANAGER),
            new Employee("Jane", "Gagarin", 1423, 28, Position.MANAGER),
            new Employee("Sam", "London", 1543, 52, Position.WORKER),
            new Employee("Jack", "Jackson", 1534, 27, Position.WORKER),
            new Employee("Eric", "Bosh", 1456, 32, Position.WORKER)
    );

    private List<Department> deps = List.of(
            new Department(1, 0, "Head"),
            new Department(2, 1, "West"),
            new Department(3, 1, "East"),
            new Department(4, 2, "Germany"),
            new Department(5, 2, "France"),
            new Department(6, 3, "China"),
            new Department(7, 3, "Japan")
    );


    @Test
    public void readThisFile() throws IOException {
        //read this file and print it to the console
        Stream<String> lines = Files.lines(Paths.get("src/test/java/Streams.java"));
        lines.forEach(System.out::println);
        System.out.println();
    }

    @Test
    public void readPath() throws IOException {
        //read path in root directory and print it to the console
        Stream<Path> list = Files.list(Paths.get("./"));
        list.forEach(System.out::println);
    }

    @Test
    public void readPathWith3Lvls() throws IOException {
        //read path in root directory and print it to the console
        Stream<Path> list = Files.walk(Paths.get("./"), 3);
        list.forEach(System.out::println);
    }

    @Test
    public void createIntAndDoubleStream() {
        IntStream intStream = IntStream.range(1, 10);
        intStream.forEach(System.out::println);

        DoubleStream doubleStream = DoubleStream.of(1.0, 2.0, 3.0);
        doubleStream.forEach(System.out::println);
    }

    @Test
    public void createStreamFromArray() {
        int[] ints = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
        IntStream stream = Arrays.stream(ints);
        stream.forEach(System.out::println);
    }

    @Test
    public void createStreamWithDifferentTypes() {
        Stream<Object> stream = Stream.of(1, "2", 3.0);
        stream.forEach(System.out::println);
    }

    @Test
    public void createStreamWithBuilder() {
        Stream<Object> stream = Stream.builder().add(1).add("2").add(3.0).build();
        stream.forEach(System.out::println);
    }

    @Test
    public void createStreamFromList() {
        Stream<Employee> stream = emps.stream();
        stream.forEach(System.out::println);
    }

    @Test
    public void createStreamGenetares() {
        Stream<Event> stream = Stream.generate(() -> new Event(UUID.randomUUID(), LocalDateTime.now(), "event"));
        stream.limit(10).forEach(System.out::println);
    }

    @Test
    public void createStreamIterate() {
        Stream<Integer> stream = Stream.iterate(1, i -> i + 10);
        stream.limit(10).forEach(System.out::println);
    }

    @Test
    public void createStreamConcat() {
        Stream<Integer> stream1 = Stream.of(1, 2, 3);
        Stream<String> stream2 = Stream.of("a  ", "b", "c");
        Stream<Object> concat = Stream.concat(stream1, stream2);
        concat.forEach(System.out::println);
    }

    @Test
    public void streamParallel() {
        int[] array = IntStream.range(1, 1000).toArray();
        Arrays.stream(array).parallel().forEach(System.out::println);
        Arrays.stream(array).parallel().forEachOrdered(System.out::println);
    }

    @Test
    public void streamCollect() {
        Map<Integer, String> collect = emps.stream().collect(Collectors.toMap(
                Employee::getId,
                emp -> String.format("%s %s ", emp.getLastName(), emp.getFirstName())
        ));
        collect.forEach((key, value) -> System.out.println("Key: " + key + ", Value: " + value));

    }

    @Test
    public void streamReduce() {
        int sum = IntStream.range(1, 10).reduce(0, (a, b) -> a + b);
        System.out.println(sum);
    }

    @Test
    public void streamReduceDeps() {
        System.out.println(deps.stream().reduce(this::reducer));
    }

    public Department reducer(Department parent, Department child) {
        if (child.getParent() == parent.getId()) {
            parent.getChild().add(child);
        } else {
            parent.getChild().forEach(dep -> reducer(dep, child));
        }
        return parent;
    }

    @Test
    public void streamFunctions() {
        System.out.println(IntStream.of(100, 200, 300, 400).average());
        System.out.println(IntStream.of(100, 200, 300, 400).min());
        System.out.println(IntStream.of(100, 200, 300, 400).max());
        System.out.println(IntStream.of(100, 200, 300, 400).sum());
        System.out.println(IntStream.of(100, 200, 300, 400).summaryStatistics());
    }

    @Test
    public void streamNoneMatch() {
        System.out.println(emps.stream().noneMatch(emp -> emp.getAge() > 60));
    }

    @Test
    public void streamAnyMatch() {
        System.out.println(emps.stream().anyMatch(emp -> emp.getPosition() == Position.CHEF));
    }

    @Test
    public void streamAllMatch() {
        System.out.println(emps.stream().allMatch(emp -> emp.getAge() > 18));
    }

    @Test
    public void transferToLong() {
        LongStream longStream = IntStream.range(1, 10).mapToLong(Long::valueOf);
        longStream.forEach(System.out::println);
    }

    @Test
    public void streamDistinct() {
        Stream.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 1, 2, 3, 4).distinct().forEach(System.out::println);
    }

    @Test
    public void streamFilter() {
        emps.stream().filter(emp -> emp.getPosition() != Position.CHEF).forEach(System.out::println);
    }

    @Test
    public void streamSkipLimit() {
        emps.stream().skip(5).limit(5).forEach(System.out::println);
    }

    @Test
    public void streamSorted() {
        emps
                .stream()
                .sorted((emp1, emp2) -> emp1.getAge() - emp2.getAge()).forEach(System.out::println);
    }

    @Test
    public void streamMap() {
        emps.stream().map(emp -> emp.getFirstName()).forEach(System.out::println);
    }

    @Test
    public void streamPeek() {
        emps.stream().peek(emp -> emp.setAge(0)).forEach(System.out::println);
    }

    @Test
    public void streamTakeWhile() {
        emps.stream().takeWhile(emp -> emp.getAge() > 30).forEach(System.out::println);
    }

    @Test
    public void streamDropWhile() {
        emps.stream().dropWhile(emp -> emp.getAge() > 30).forEach(System.out::println);
    }

    @Test
    public void streamFlatMap() {
        IntStream.of(100, 200, 300, 400)
                .flatMap(value -> IntStream.of(value - 50, value))
                .forEach(System.out::println);
    }
}
