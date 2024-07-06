package com.unimatch.unimatch_backend.common.websocket.interceptor;

import lombok.extern.slf4j.Slf4j;
import com.unimatch.unimatch_backend.common.shiro.util.JwtUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@Slf4j
public class SocketInterceptor implements HandshakeInterceptor {
    @Override
    public boolean beforeHandshake(ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse, WebSocketHandler webSocketHandler, Map<String, Object> map) throws Exception {
        if (serverHttpRequest instanceof ServletServerHttpRequest) {
            HttpServletRequest servletRequest = ((ServletServerHttpRequest) serverHttpRequest).getServletRequest();
            // Obtain token and username from the header
            String params = servletRequest.getHeader("Sec-WebSocket-Protocol");
            if (StringUtils.isNotBlank(params)) {
                //token:0, username:1
                String[] param = params.split("\\|");
                return JwtUtil.verify(param[0], param[1]);
            }
        }
        return true;
    }

    @Override
    public void afterHandshake(ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse, WebSocketHandler webSocketHandler, Exception e) {
        if (serverHttpRequest instanceof ServletServerHttpRequest) {
            HttpServletRequest req = ((ServletServerHttpRequest) serverHttpRequest).getServletRequest();
            HttpServletResponse resp = ((ServletServerHttpResponse) serverHttpResponse).getServletResponse();
            if (!StringUtils.isEmpty(req.getHeader("Sec-WebSocket-Protocol"))) {
                // Must return to frontend, or it will throw an error(cannot connect to websocket)
                resp.addHeader("Sec-WebSocket-Protocol", req.getHeader("Sec-WebSocket-Protocol"));
            }
        }
    }
}
