/*
 * Copyright parcIT GmbH
 *
 * Dieser Source-Code steht unter dem alleinigen Urheberschutz der parcIT GmbH.
 * Die Nutzung und Weitergabe ist nur mit ausdrücklicher Erlaubnis der parcIT GmbH gestattet.
 *
 */

package com.eb.apps.ebchat_client.domain.context.app;
import javax.swing.*;


public class PromptDesignApp {

    public static void main(String[] args) {

        SwingUtilities.invokeLater(PromptDesignDlg::Show);

    }
}
