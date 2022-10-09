package test;
public class test {
    public static void main(String[] args) {
        Object test = 0;
        System.out.println(test);
        System.out.println(test.getClass().getSimpleName());
        test += "hello";
        System.out.println(test);
        System.out.println(test.getClass().getSimpleName());
        test = 0;
        System.out.println(test.getClass().getSimpleName());
    }
}
