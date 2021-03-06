import webbrowser

import gi

import info
import net
from data import save, load
from net import Account, login_smart, login

gi.require_version("Gtk", "3.0")
from gi.repository import Gtk,Gdk,GdkPixbuf


class Login(Gtk.Window):
    def visit(self, btn):
        webbrowser.open(btn.get_uri())
        return True

    def __init__(self):
        Gtk.Window.__init__(self,
                            title=info.NAME)
        self.set_size_request(300, 400)
        self.set_icon_from_file ("ic_launcher.png")
        self.connect("destroy", Gtk.main_quit)
        menubar = Gtk.MenuBar()
        menuitem_help = Gtk.MenuItem("Help")
        menu_help = Gtk.Menu()
        menuitem_help.set_submenu(menu_help)
        menuitem_about = Gtk.MenuItem("about")
        menuitem_about.connect("activate", self.about)
        menu_help.append(menuitem_about)
        menubar.append(menuitem_help)
        button_changepassword = Gtk.LinkButton(label="change password?")
        button_changepassword.props.uri = "http://10.0.62.7:8080/Self/login/"
        button_changepassword.connect("activate-link", self.visit)
        vbox = Gtk.VBox(False, 2)
        self.entry_number = Gtk.Entry()
        self.entry_password = Gtk.Entry()
        self.entry_password.props.placeholder_text = "password"
        self.entry_password.props.visibility=False
        self.entry_password.set_invisible_char('*')
        self.entry_number.props.placeholder_text = "number"
        self.checkbutton_autoconnect = Gtk.CheckButton(label="auto connect")
        self.checkbutton_savepassword = Gtk.CheckButton(label="save password")
        self.list_nettypes = ["hints", "telecom", "campus"]
        self.combobox_types = Gtk.ComboBoxText()
        for type in self.list_nettypes:
            self.combobox_types.append_text(type)
        self.combobox_types.props.active = 0
        hbox = Gtk.HBox()
        hbox.pack_start(self.checkbutton_autoconnect, False, False, 0)
        hbox.pack_end(self.checkbutton_savepassword, False, False, 0)
        button_login = Gtk.Button(label="login")
        button_login.connect("clicked", self.login)
        vbox.pack_start(menubar, False, False, 0)
        vbox.pack_start(self.entry_number, False, False, 0)
        vbox.pack_start(self.entry_password, False, False, 0)
        vbox.pack_start(self.combobox_types, False, False, 0)
        vbox.pack_start(hbox, False, False, 0)
        vbox.pack_start(button_login, False, False, 2)
        vbox.pack_end(button_changepassword, False, False, 0)
        self.load()
        self.add(vbox)

    def login(self, widget):
        account = Account(password=self.entry_password.props.text,
                          number=self.entry_number.props.text
                          )
        if self.combobox_types.props.active ==1:
            account.type = Account.TELECOM
        elif self.combobox_types.props.active==2:
            account.type = Account.CAMPUS
        else:
            print("erro")
        if self.checkbutton_savepassword.props.active:
            save(account)
        self.login_notify(login(account,self.checkbutton_autoconnect.props.active))

    def load(self):
        account = load()
        self.entry_number.props.text = account.number
        self.entry_password.props.text = account.password
        if account.type == Account.CAMPUS:
            self.combobox_types.props.active = 2
        else:
            self.combobox_types.props.active = 1

    def about(self, widget):
        about = Gtk.AboutDialog()
        about.set_icon_from_file ("ic_launcher.png")
        about.set_program_name(info.NAME)
        about.set_version(info.VERSION)
        about.set_copyright("(c) Joseph Blue")
        about.set_comments("python编写的益阳职业技术学院的第三方校园网图形客户端")
        about.set_website(info.URL)
        about.set_logo(GdkPixbuf.Pixbuf.new_from_file("ic_launcher.png"))
        about.connect("activate-link", lambda x, y: webbrowser.open(y))
        about.run()
        about.destroy()

    def login_notify(self,status):
        dialog=Gtk.MessageDialog(self,0,Gtk.MessageType.INFO,Gtk.ButtonsType.OK,"Notice")
        if status:
           dialog.format_secondary_text("Failure")
           dialog.run()
           dialog.destroy()


