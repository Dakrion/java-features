import org.junit.jupiter.api.Test;

public class ParTest {

    @Test
    void test1() throws InterruptedException {
        Thread.sleep(2000);
        System.out.println("Test1");
    }

    @Test
    void test2() throws InterruptedException {
        Thread.sleep(2000);
        System.out.println("Test2" + System.getenv("parallel"));
    }
}
