import subprocess
import ui
import requests
import gi
gi.require_version("Gtk", "3.0")
from gi.repository import Gtk
NAME="YourNetwork"
VERSION="alpha"
win =ui.Login()
win.show_all()
Gtk.main()
