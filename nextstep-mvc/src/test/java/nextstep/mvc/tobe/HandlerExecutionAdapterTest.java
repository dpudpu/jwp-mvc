package nextstep.mvc.tobe;


import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

class HandlerExecutionAdapterTest {
    private final HandlerAdapter handlerAdapter = new HandlerExecutionAdapter();
    private final HandlerExecution handler = mock(HandlerExecution.class);

    @Test
    @DisplayName("supports() HandlerExecution 지원 확인")
    void supportsTest() {
        // when & then
        assertThat(handlerAdapter.supports(handler)).isTrue();
    }

    //todo 나중에 테스트하기
//    @Test
//    @DisplayName("handle() String to ModelAndView ")
//    void StringToModelAndView() throws Exception {
//        // given
//        when(handler.execute(any())).thenReturn("view");
//
//        // when & then
//        assertDoesNotThrow(() -> handlerAdapter.handle(any(), any(), handler));
//    }
//
//    @Test
//    @DisplayName("handle() 지원하지 않는 값을 리턴 받을 경우 예외처리")
//    void handleTest() throws Exception {
//        // given
//        when(handler.execute(any(), any())).thenReturn(123);
//
//        // when & then
//        assertThrows(ClassCastException.class, () -> handlerAdapter.handle(any(), any(), handler));
//    }
}
