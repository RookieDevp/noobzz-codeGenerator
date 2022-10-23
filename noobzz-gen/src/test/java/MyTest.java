import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author: ZZJ
 * @date: 2022/10/14
 * @desc:
 */
public class MyTest {
    public static void main(String[] args) {

        String [] arr = {"东","南","西","北"};
        Stream<String> stream = Arrays.stream(arr);
        List<String> list = Arrays.asList("aa","cc","bb","aa","dd");
        List<String> collect = list.stream().sorted().collect(Collectors.toList());
        list.stream().sorted().forEach(System.out::println);
        list.stream().map(s -> s.length()).forEach(System.out::println);
        List<Integer> collect1 = list.stream().filter(s -> s.equals("aa")).map(s -> s.length()).collect(Collectors.toList());
        boolean aa = list.stream().anyMatch(s -> s.equals("aa"));
        System.out.println("aa = " + aa);
        List<Integer> integers = Arrays.asList(1, 2, 5, 3, 4, 7);
        integers.stream().filter(integer -> integer > 4).forEach(System.out::println);

    }
}
