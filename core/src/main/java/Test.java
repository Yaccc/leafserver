import lombok.AllArgsConstructor;
import lombok.Builder;

/**
 * Created by XIEzhaodong PACKAGE_NAME on 下午5:36
 */
@Builder
public class Test {

    private String name;

    public static void main(String[] args) {
        Test
                .builder().build();
    }

}
