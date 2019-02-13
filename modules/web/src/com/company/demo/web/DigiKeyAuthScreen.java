package com.company.demo.web;

import com.company.demo.service.DigiKeyService;
import com.google.common.base.Strings;
import com.haulmont.cuba.gui.Route;
import com.haulmont.cuba.gui.components.Button;
import com.haulmont.cuba.gui.components.TextArea;
import com.haulmont.cuba.gui.executors.BackgroundWorker;
import com.haulmont.cuba.gui.executors.UIAccessor;
import com.haulmont.cuba.gui.screen.Screen;
import com.haulmont.cuba.gui.screen.Subscribe;
import com.haulmont.cuba.gui.screen.UiController;
import com.haulmont.cuba.gui.screen.UiDescriptor;
import com.vaadin.server.*;
import org.slf4j.Logger;

import javax.inject.Inject;
import java.io.IOException;
import java.net.URI;

@UiController("demo_DigiKeyAuthScreen")
@UiDescriptor("digikey-auth-screen.xml")
@Route("digikey-auth-screen")
public class DigiKeyAuthScreen extends Screen {

    @Inject
    protected Logger log;

    @Inject
    protected BackgroundWorker backgroundWorker;

    @Inject
    protected TextArea<String> statusField;

    @Inject
    protected DigiKeyService digiKeyService;

    protected RequestHandler digiKeyRequestHandler;

    protected UIAccessor uiAccessor;

    protected URI redirectUri;

    @Subscribe
    protected void onAfterShow(AfterShowEvent event) {
        uiAccessor = backgroundWorker.getUIAccessor();
    }

    @Subscribe("authenticateBtn")
    protected void onAuthenticateBtnClick(Button.ClickEvent event) {
        //register the request handler . It will handle the callback from the DigiKey authorization page
        digiKeyRequestHandler = this::handleRequest;
        VaadinSession.getCurrent().addRequestHandler(digiKeyRequestHandler);
        redirectUri = Page.getCurrent().getLocation();
        //open the DigiKey authorization page
        Page.getCurrent().setLocation(digiKeyService.getAuthorizationCodeEndpointUrl());
    }

    @Subscribe("refreshTokenBtn")
    protected void onRefreshTokenBtnClick(Button.ClickEvent event) {
        digiKeyService.obtainAccessTokenByRefreshToken();
    }

    private boolean handleRequest(VaadinSession session, VaadinRequest request, VaadinResponse response) throws IOException {
        if (request.getParameter("code") != null) {
            uiAccessor.accessSynchronously(() -> {
                try {
                    String code = request.getParameter("code");
                    String result = digiKeyService.obtainAccessTokenByCode(code);
                    statusField.setValue(result);
                } catch (Exception e) {
                    log.error("Error on login to DigiKey", e);
                } finally {
                    session.removeRequestHandler(digiKeyRequestHandler);
                }
            });
            //make a redirect to the current page
            ((VaadinServletResponse) response).getHttpServletResponse().sendRedirect(redirectUri.toString());
            return true;
        }
        return false;
    }
}