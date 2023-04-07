
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.OverlayLayout;

import org.cef.CefApp;
import org.cef.CefApp.CefAppState;
import org.cef.CefClient;
import org.cef.CefSettings;
import org.cef.browser.CefBrowser;
import org.cef.handler.CefAppHandlerAdapter;

/**
 * This is my attempt to show a non-opaque image over a OSR JCEF browser.  Instead I get a grey square where
 * the JLabel is rendered.
 * portion of the image */
public class JCEF extends JFrame {
    private final CefApp cefApp_;
    private final CefClient client_;
    private final CefBrowser browser_;
    private final Component browerUI_;

    private JCEF(String startURL, boolean useOSR, boolean isTransparent) {
        CefApp.addAppHandler(new CefAppHandlerAdapter(null) {
            @Override
            public void stateHasChanged(org.cef.CefApp.CefAppState state) {
                if (state == CefAppState.TERMINATED) System.exit(0);
            }
        });
        CefSettings settings = new CefSettings();
        settings.windowless_rendering_enabled = true;
        cefApp_ = CefApp.getInstance(settings);
        client_ = cefApp_.createClient();
        browser_ = client_.createBrowser(startURL, useOSR, isTransparent);
        browerUI_ = browser_.getUIComponent();

        JPanel p = new JPanel();
        p.setLayout(new OverlayLayout(p));
        JLabel label = new JLabel("hello world");
        label.setSize(new Dimension(100,100));
        label.setMaximumSize(label.getSize());
        label.setMinimumSize(label.getSize());
        label.setBackground(new Color(0,0,0,0));
        label.setOpaque(false);
        p.add(label);
        
        // Changing this to false will display a normal JPanel, and you can see the JLabel doesn't have a 
        // grey border.
        if (true) {
            p.add(browerUI_);
        } else {
            JPanel panel = new JPanel();
            panel.setBackground(Color.YELLOW);
            p.add(panel);
        }
                                    
        getContentPane().add(p);
        pack();
        setSize(800, 600);
        setVisible(true);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                CefApp.getInstance().dispose();
                dispose();
                System.exit(0);
            }
        });
    }

    public static void main(String[] args) {
        if (!CefApp.startup(args)) {
            return;
        }
        new JCEF("https://www.google.com", true, false);
    }
}