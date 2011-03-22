package net.it_tim;

import java.io.IOException;

import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Control;

public class Settings extends Dialog {

	protected Object result;
	protected Shell shell;
	private Text text;
	private Text text_1;
	private Text text_2;
	private Text text_3;
	private SettingsLoader prefs;

	/**
	 * Create the dialog.
	 * @param parent
	 * @param style
	 */
	public Settings(Shell parent, int style) {
		super(parent, style);
		setText("Настройки");
	}

	/**
	 * Open the dialog.
	 * @return the result
	 */
	public Object open() {
		prefs = new SettingsLoader(".zayavku/config");
		try {	
			prefs.loadConfig();
			createContents();
			shell.open();
			shell.layout();
			Display display = getParent().getDisplay();
			while (!shell.isDisposed()) {
				if (!display.readAndDispatch()) {
					display.sleep();
				}
			}
			return result;
		} catch (IOException ex) {
			ex.printStackTrace();
			shell = new Shell(getParent(), SWT.NONE);
			MessageBox messageBox = new MessageBox(shell, SWT.ICON_ERROR | SWT.OK);
	        messageBox.setText("IO Error");
	        messageBox.setMessage(ex.getMessage());
	        messageBox.open();
	        shell.dispose();
			return 0;
		}
	}

	/**
	 * Create contents of the dialog.
	 */
	private void createContents() {
		shell = new Shell(getParent(), SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL);
		shell.setSize(307, 265);
		shell.setText("Налаштування");
		shell.setLayout(new FormLayout());
		
		Button button = new Button(shell, SWT.NONE);
		FormData fd_button = new FormData();
		fd_button.top = new FormAttachment(0, 196);
		fd_button.left = new FormAttachment(0, 211);
		button.setLayoutData(fd_button);
		button.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				prefs.setOption("host", text.getText());
				prefs.setOption("port", text_1.getText());
				prefs.setOption("login", text_2.getText());
				prefs.setOption("password", text_3.getText());
				if (prefs.saveConfig()) {
					MessageBox messageBox = new MessageBox(shell, SWT.ICON_ERROR | SWT.OK);
			        messageBox.setText("Перезагрузка");
			        messageBox.setMessage("Потрібно перезапустити програму!");
			        messageBox.open();
					System.exit(0);
				} else {
					MessageBox messageBox = new MessageBox(shell, SWT.ICON_WARNING | SWT.OK);
			        messageBox.setText("IO Error");
			        messageBox.setMessage("Can't save config, unrecognized error");
			        messageBox.open();
			        System.exit(SWT.ERROR);
				}
			}
		});
		button.setText("Зберегти");
		
		Group group = new Group(shell, SWT.NONE);
		FormData fd_group = new FormData();
		fd_group.bottom = new FormAttachment(0, 93);
		fd_group.right = new FormAttachment(0, 287);
		fd_group.top = new FormAttachment(0, 10);
		fd_group.left = new FormAttachment(0, 10);
		group.setLayoutData(fd_group);
		group.setText("Налаштування підключення");
		
		Label label = new Label(group, SWT.NONE);
		label.setBounds(10, 22, 55, 17);
		label.setText("Хост БД");
		
		text = new Text(group, SWT.BORDER);
		text.setBounds(10, 45, 125, 27);
		text.setText(prefs.getOption("host", "localhost"));
		
		Label label_1 = new Label(group, SWT.NONE);
		label_1.setBounds(141, 22, 70, 17);
		label_1.setText("Порт");
		
		text_1 = new Text(group, SWT.BORDER);
		text_1.setBounds(141, 45, 125, 27);
		text_1.setText(prefs.getOption("port", "5432"));
		
		group.setTabList(new Control[]{text, text_1});
		
		Group group_1 = new Group(shell, SWT.NONE);
		FormData fd_group_1 = new FormData();
		fd_group_1.bottom = new FormAttachment(0, 190);
		fd_group_1.right = new FormAttachment(0, 287);
		fd_group_1.top = new FormAttachment(0, 99);
		fd_group_1.left = new FormAttachment(0, 10);
		group_1.setLayoutData(fd_group_1);
		group_1.setText("Користувач");
		
		Label label_2 = new Label(group_1, SWT.NONE);
		label_2.setBounds(10, 24, 70, 17);
		label_2.setText("Логін");
		
		text_2 = new Text(group_1, SWT.BORDER);
		text_2.setBounds(10, 47, 125, 27);
		text_2.setText(prefs.getOption("login", "postgres"));
		
		text_3 = new Text(group_1, SWT.BORDER | SWT.PASSWORD);
		text_3.setBounds(141, 47, 125, 27);
		text_3.setText("123456789");
		
		Label label_3 = new Label(group_1, SWT.NONE);
		label_3.setBounds(136, 24, 70, 17);
		label_3.setText("Пароль");
		shell.setTabList(new Control[]{group, group_1, button});

	}
}
