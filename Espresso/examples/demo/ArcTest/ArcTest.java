/*
 * @(#)ArcTest.java	1.4 97/02/05
 *
 * Copyright (c) 1997 Sun Microsystems, Inc. All Rights Reserved.
 *
 * Sun grants you ("Licensee") a non-exclusive, royalty free, license to use,
 * modify and redistribute this software in source and binary code form,
 * provided that i) this copyright notice and license appear on all copies of
 * the software; and ii) Licensee does not utilize the software in a manner
 * which is disparaging to Sun.
 *
 * This software is provided "AS IS," without a warranty of any kind. ALL
 * EXPRESS OR IMPLIED CONDITIONS, REPRESENTATIONS AND WARRANTIES, INCLUDING ANY
 * IMPLIED WARRANTY OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE OR
 * NON-INFRINGEMENT, ARE HEREBY EXCLUDED. SUN AND ITS LICENSORS SHALL NOT BE
 * LIABLE FOR ANY DAMAGES SUFFERED BY LICENSEE AS A RESULT OF USING, MODIFYING
 * OR DISTRIBUTING THE SOFTWARE OR ITS DERIVATIVES. IN NO EVENT WILL SUN OR ITS
 * LICENSORS BE LIABLE FOR ANY LOST REVENUE, PROFIT OR DATA, OR FOR DIRECT,
 * INDIRECT, SPECIAL, CONSEQUENTIAL, INCIDENTAL OR PUNITIVE DAMAGES, HOWEVER
 * CAUSED AND REGARDLESS OF THE THEORY OF LIABILITY, ARISING OUT OF THE USE OF
 * OR INABILITY TO USE SOFTWARE, EVEN IF SUN HAS BEEN ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGES.
 *
 * This software is not designed or intended for use in on-line control of
 * aircraft, air traffic, aircraft navigation or aircraft communications; or in
 * the design, construction, operation or maintenance of any nuclear
 * facility. Licensee represents and warrants that it will not use or
 * redistribute the Software for such purposes.
 */

import java.awt.*;
import java.awt.event.*;
import java.applet.*;

/**
 * An interactive test of the Graphics.drawArc and Graphics.fillArc
 * routines. Can be run either as a standalone application by
 * typing "java ArcTest" or as an applet in the AppletViewer.
 */
public class ArcTest extends Applet {
    ArcControls controls;   // The controls for marking and filling arcs
    ArcCanvas canvas;       // The drawing area to display arcs

    public void init() {
	setLayout(new BorderLayout());
	canvas = new ArcCanvas();
	add("Center", canvas);
	add("South", controls = new ArcControls(canvas));
    }

    public void destroy() {
        remove(controls);
        remove(canvas);
    }

    public void start() {
	controls.setEnabled(true);
    }

    public void stop() {
	controls.setEnabled(false);
    }
  
    public void processEvent(AWTEvent e) {
        if (e.getID() == Event.WINDOW_DESTROY) {
            System.exit(0);
        }
    }

    public static void main(String args[]) {
	Frame f = new Frame("ArcTest");
	ArcTest	arcTest = new ArcTest();

	arcTest.init();
	arcTest.start();

	f.add("Center", arcTest);
	f.setSize(300, 300);
	f.show();
    }

    public String getAppletInfo() {
        return "An interactive test of the Graphics.drawArc and \nGraphics.fillArc routines. Can be run \neither as a standalone application by typing 'java ArcTest' \nor as an applet in the AppletViewer.";
    }
}

class ArcCanvas extends Canvas {
    int		startAngle = 0;
    int		endAngle = 45;
    boolean	filled = false;
    Font	font;

    public void paint(Graphics g) {
	Rectangle r = getBounds();
	int hlines = r.height / 10;
	int vlines = r.width / 10;

	g.setColor(Color.pink);
	for (int i = 1; i <= hlines; i++) {
	    g.drawLine(0, i * 10, r.width, i * 10);
	}
	for (int i = 1; i <= vlines; i++) {
	    g.drawLine(i * 10, 0, i * 10, r.height);
	}

	g.setColor(Color.red);
	if (filled) {
	    g.fillArc(0, 0, r.width - 1, r.height - 1, startAngle, endAngle);
	} else {
	    g.drawArc(0, 0, r.width - 1, r.height - 1, startAngle, endAngle);
	}

	g.setColor(Color.black);
	g.setFont(font);
	g.drawLine(0, r.height / 2, r.width, r.height / 2);
	g.drawLine(r.width / 2, 0, r.width / 2, r.height);
	g.drawLine(0, 0, r.width, r.height);
	g.drawLine(r.width, 0, 0, r.height);
	int sx = 10;
	int sy = r.height - 28;
	g.drawString("S = " + startAngle, sx, sy); 
	g.drawString("E = " + endAngle, sx, sy + 14); 
    }

    public void redraw(boolean filled, int start, int end) {
	this.filled = filled;
	this.startAngle = start;
	this.endAngle = end;
	repaint();
    }
}

class ArcControls extends Panel
                  implements ActionListener {
    TextField s;
    TextField e;
    ArcCanvas canvas;

    public ArcControls(ArcCanvas canvas) {
	Button b = null;

	this.canvas = canvas;
	add(s = new TextField("0", 4));
	add(e = new TextField("45", 4));
	b = new Button("Fill");
	b.addActionListener(this);
	add(b);
	b = new Button("Draw");
	b.addActionListener(this);
	add(b);
    }

    public void actionPerformed(ActionEvent ev) {
	String label = ev.getActionCommand();

	canvas.redraw(label.equals("Fill"),
	              Integer.parseInt(s.getText().trim()),
	              Integer.parseInt(e.getText().trim()));
    }
}
	
