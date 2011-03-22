package net.it_tim;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Monitor;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Combo;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;

import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.*;

import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormAttachment;

public class Zayavku {

	protected Shell shell;
	private static Table table;
	private static Combo combo;
	private static Connection conn;

	private static Image trueImage;
	private static Image falseImage;
	private static Display display;
	private static SettingsLoader prefs;
	/**
	 * Launch the application.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			prefs = new SettingsLoader(".zayavku/config");
			try {
				prefs.loadConfig();
			} catch (IOException ex) {
				System.out.println(ex.getMessage());
			}
			db_connect();
			display = new Display();
			Zayavku window = new Zayavku();
			window.open();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Open the window.
	 */
	public void open() {
		Display display = Display.getDefault();
		createContents();
		shell.open();
		shell.layout();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}

	/**
	 * Create contents of the window.
	 */
	protected void createContents() {
		shell = new Shell(display);
		shell.setSize(1304, 900);
		shell.setText("Заявки");
		shell.setLayout(new FormLayout());

		try {
			InputStream true_image = Zayavku.class.getClassLoader()
					.getResourceAsStream("icon/OK.png");
			InputStream false_image = Zayavku.class.getClassLoader()
					.getResourceAsStream("icon/Denied.png");
			trueImage = new Image(display, true_image);
			falseImage = new Image(display, false_image);
		} catch (IllegalArgumentException ex) {
			MessageBox messageBox = new MessageBox(shell, SWT.ICON_WARNING | SWT.OK);
	        messageBox.setText("IO Error");
	        messageBox.setMessage("Icon file not found");
	        messageBox.open();
			System.out.println(ex.getMessage());
		}
		
		Monitor primary = display.getPrimaryMonitor();
		Rectangle bounds = primary.getBounds();
		Rectangle rect = shell.getBounds();

		int x = bounds.x + (bounds.width - rect.width) / 2;
		int y = bounds.y + (bounds.height - rect.height) / 2;

		shell.setLocation(x, y);

		Menu menu = new Menu(shell, SWT.BAR);
		shell.setMenuBar(menu);

		MenuItem mntmNewSubmenu = new MenuItem(menu, SWT.CASCADE);
		mntmNewSubmenu.setText("File");

		Menu menu_1 = new Menu(mntmNewSubmenu);
		mntmNewSubmenu.setMenu(menu_1);

		MenuItem mntmExit_2 = new MenuItem(menu_1, SWT.NONE);
		
		mntmExit_2.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				System.exit(0);
			}
		});
		
		mntmExit_2.setText("Exit");

		MenuItem mntmEdit = new MenuItem(menu, SWT.CASCADE);
		mntmEdit.setText("Edit");

		Menu menu_2 = new Menu(mntmEdit);
		mntmEdit.setMenu(menu_2);

		MenuItem mntmSettings = new MenuItem(menu_2, SWT.NONE);
		
		mntmSettings.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				Settings settingsDialog = new Settings(shell, 0);
				settingsDialog.open();
			}
		});
		
		mntmSettings.setText("Settings");

		table = new Table(shell, SWT.BORDER | SWT.FULL_SELECTION | SWT.MULTI);
		FormData fd_table = new FormData();
		fd_table.bottom = new FormAttachment(100, -10);
		fd_table.left = new FormAttachment(0, 10);
		fd_table.right = new FormAttachment(100, -5);
		table.setLayoutData(fd_table);
		table.setHeaderVisible(true);
		table.setLinesVisible(true);

		TableColumn tblclmnNewColumn_1 = new TableColumn(table, SWT.LEFT);

		tblclmnNewColumn_1.setWidth(150);
		tblclmnNewColumn_1.setText("Вулиця");

		TableColumn tblclmnNewColumn = new TableColumn(table, SWT.LEFT);
		tblclmnNewColumn.setWidth(100);
		tblclmnNewColumn.setText("Кв.");

		TableColumn tableColumn = new TableColumn(table, SWT.LEFT);
		tableColumn.setWidth(100);
		tableColumn.setText("Логін");

		TableColumn tableColumn_1 = new TableColumn(table, SWT.LEFT);
		tableColumn_1.setWidth(100);
		tableColumn_1.setText("Помилка");

		TableColumn tableColumn_2 = new TableColumn(table, SWT.LEFT);
		tableColumn_2.setWidth(120);
		tableColumn_2.setText("Дата виклику");

		TableColumn tableColumn_3 = new TableColumn(table, SWT.LEFT);
		tableColumn_3.setWidth(100);
		tableColumn_3.setText("Час Виклику");

		TableColumn tableColumn_4 = new TableColumn(table, SWT.LEFT);
		tableColumn_4.setWidth(54);
		tableColumn_4.setText("Статус");

		TableColumn tableColumn_5 = new TableColumn(table, SWT.LEFT);
		tableColumn_5.setWidth(62);
		tableColumn_5.setText("Д.тел.");

		TableColumn tableColumn_6 = new TableColumn(table, SWT.LEFT);
		tableColumn_6.setWidth(100);
		tableColumn_6.setText("Моб.тел.");

		TableColumn tblclmnNewColumn_2 = new TableColumn(table, SWT.LEFT);
		tblclmnNewColumn_2.setWidth(150);
		tblclmnNewColumn_2.setText("Виконавець");

		TableColumn tableColumn_7 = new TableColumn(table, SWT.LEFT);
		tableColumn_7.setWidth(110);
		tableColumn_7.setText("Коментарі");

		Group group = new Group(shell, SWT.NONE);
		fd_table.top = new FormAttachment(group, 6);
		group.setText("Статус");
		group.setLayout(new FormLayout());
		FormData fd_group = new FormData();
		fd_group.left = new FormAttachment(0, 10);
		group.setLayoutData(fd_group);

		combo = new Combo(group, SWT.NONE);
		fd_group.top = new FormAttachment(combo, 3);
		FormData fd_combo = new FormData();
		fd_combo.top = new FormAttachment(0);
		fd_combo.left = new FormAttachment(0);
		fd_combo.right = new FormAttachment(0, 183);
		combo.setLayoutData(fd_combo);
		
		combo.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				refresh_grid(combo.getSelectionIndex());
			}
		});
		
		combo.setItems(new String[] { "Всі", "Виконані", "Не виконані" });
		combo.select(2);
		refresh_grid(2);
		shell.setTabList(new Control[] { table });
	}

	public static void db_connect() {
		try {
			String url = "jdbc:postgresql://" + prefs.getOption("host", "localhost") + ":" +prefs.getOption("port", "5432") + "/zayavka";
			Properties props = new Properties();
			props.setProperty("user", prefs.getOption("login", "postgres"));
			props.setProperty("password", prefs.getOption("password", "123456"));
			conn = DriverManager.getConnection(url, props);
		} catch (SQLException e) {
			System.out.println("!!!Connection error!!!");
			System.out.println(e.getMessage());
		}
	}

	private static void select_all(int status) {
		String stat = new String();
		Color red = new Color(display, 255, 204, 204);
		switch (status) {
		case 0:
			stat = "";
			break;
		case 1:
			stat = "AND zayavka_zayavka.status = true";
			break;
		case 2:
			stat = "AND zayavka_zayavka.status = false";
			break;
		}

		try {
			Statement st = conn.createStatement();
			ResultSet rs = st
					.executeQuery("SELECT sorting, kv, login, zayavka_error.name, date, zayavka_time_choices.descr ,status, domtel, mobtel, zayavka_worker.name, comments "
							+ "FROM zayavka_zayavka, zayavka_dom, zayavka_error, zayavka_worker, zayavka_time_choices "
							+ "WHERE zayavka_dom.id = zayavka_zayavka.vyl_id "
							+ "AND zayavka_zayavka.error_id = zayavka_error.id "
							+ "AND zayavka_zayavka.who_do_id = zayavka_worker.id "
							+ "AND zayavka_time_choices.time_id = zayavka_zayavka.time "
							+ stat);
			while (rs.next()) {
				TableItem tableItem = new TableItem(table, SWT.NONE);

				tableItem.setText(0, rs.getString(1));
				tableItem.setBackground(0, red);
				tableItem.setText(1, rs.getString(2));
				tableItem.setText(2, rs.getString(3));
				tableItem.setText(3, rs.getString(4));

				try {
					tableItem.setText(4, rs.getDate(5).toString());
				} catch (NullPointerException e) {
					tableItem.setText(4, "Немає");
				}

				try {
					tableItem.setText(5, rs.getString(6));
				} catch (IllegalArgumentException e) {
					tableItem.setText(5, "Немає");
				}

				// tableItem.setText(6, new
				// Boolean(rs.getBoolean(7)).toString());
				if (rs.getBoolean(7)) {
					tableItem.setImage(6, trueImage);
				} else {
					tableItem.setImage(6, falseImage);
				}

				tableItem.setText(7, rs.getString(8));
				tableItem.setText(8, rs.getString(9));
				tableItem.setText(9, rs.getString(10));
				tableItem.setText(10, rs.getString(11));
			}
			st.close();
			rs.close();
		} catch (NullPointerException e) {
			System.out.println(e.getMessage());
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}

	private static void refresh_grid(int status) {
		table.removeAll();
		select_all(status);
	}
}
