package nextstep.mvc;

import nextstep.mvc.tobe.DefaultHandlerAdapter;
import nextstep.mvc.tobe.Handler;
import nextstep.mvc.tobe.HandlerAdapter;
import nextstep.mvc.tobe.ModelAndView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@WebServlet(name = "dispatcher", urlPatterns = "/", loadOnStartup = 1)
public class DispatcherServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final Logger logger = LoggerFactory.getLogger(DispatcherServlet.class);
    private static final String DEFAULT_REDIRECT_PREFIX = "redirect:";

    private List<HandlerMapping> handlerMappings;
    private List<HandlerAdapter> handlerAdapters;

    public DispatcherServlet(HandlerMapping... handlerMappings) {
        this.handlerMappings = Arrays.asList(handlerMappings);
        this.handlerAdapters = Arrays.asList(new DefaultHandlerAdapter());
    }

    @Override
    public void init() throws ServletException {
        handlerMappings.forEach(HandlerMapping::initialize);
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            logger.debug("Method : {}, Request URI : {}", req.getMethod(), req.getRequestURI());
            // todo 404
            final Handler handler = handlerMappings.stream()
                    .map(handlerMapping -> handlerMapping.getHandler(req))
                    .filter(Objects::nonNull)
                    .findFirst()
                    .orElseThrow(UnsupportedOperationException::new);

            // TODO Exception
            final HandlerAdapter handlerAdapter = handlerAdapters.stream()
                    .filter(adapter -> adapter.supports(handler))
                    .findAny()
                    .orElseThrow(UnsupportedOperationException::new);

            final ModelAndView mav = handlerAdapter.handle(req, resp, handler);

            // TODO ViewResolver (2단계)
            move(mav, req, resp);
        }catch (Exception e){
            logger.error(e.getMessage());
        }
    }

    private void move(ModelAndView mav, HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        final String viewName = mav.getViewName();
        if (viewName.startsWith(DEFAULT_REDIRECT_PREFIX)) {
            resp.sendRedirect(viewName.substring(DEFAULT_REDIRECT_PREFIX.length()));
            return;
        }
        RequestDispatcher rd = req.getRequestDispatcher(viewName);
        rd.forward(req, resp);
    }
}
