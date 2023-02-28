package hello.jdbc.exception.basic;

import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

@Slf4j
public class UncheckedTest {

    /**
     * runtimeException을 상속받은 예외는 언체크 예외가 된다
     */

    @Test
    void unchecked_catch(){
        Service service = new Service();
        service.callCatch();
    }

    @Test
    void unchecked_Throw(){
        Service service = new Service();
        Assertions.assertThatThrownBy(()->service.callCatch())
                .isInstanceOf(MyUncheckedException.class);
    }

    static class MyUncheckedException extends RuntimeException {
        public MyUncheckedException(String message) {
            super(message);
        }
    }

    static class Repository {
        public void call() {
            throw new MyUncheckedException("ex");
        }
    }

    static class Service {
        Repository repository = new Repository();

        public void callCatch() {
            try {
                repository.call();
            }catch (MyUncheckedException e){
                log.info("예외 처리, message ={}", e.getMessage());
            }
        }

        public void callThrow(){
            repository.call();
        }
    }
}
