'''
Created on Mar 17, 2016

@author: eyaomai
'''

from wsgiref.simple_server import WSGIRequestHandler,ServerHandler
from wsgiref.handlers import format_date_time
import time
class RestRequestHandler(WSGIRequestHandler):
    def handle(self):
        """Handle a single HTTP request"""

        self.raw_requestline = self.rfile.readline(65537)
        if len(self.raw_requestline) > 65536:
            self.requestline = ''
            self.request_version = ''
            self.command = ''
            self.send_error(414)
            return

        if not self.parse_request(): # An error code has been sent, just exit
            return

        handler = RestSimpleServerHandler(
            self.rfile, self.wfile, self.get_stderr(), self.get_environ()
        )
        handler.request_handler = self      # backpointer for logging
        handler.run(self.server.get_app())

class RestSimpleServerHandler(ServerHandler):

    def send_preamble(self):
        """Transmit version/status/date/server, via self._write()"""
        if self.origin_server:
            if self.client_is_modern():
                self._write('HTTP/%s %s\r\n' % (self.http_version,self.status))
                if 'Date' not in self.headers:
                    self._write(
                        'Date: %s\r\n' % format_date_time(time.time())
                    )

                if self.server_software and 'Server' not in self.headers:
                    self._write('Server: %s\r\n' % self.server_software)
                # reggie: add these for ajax cross domain request
                self._write('Access-Control-Allow-Origin: %s\r\n' % '*')
                self._write('Access-Control-Allow-Methods: %s\r\n' % 'GET, POST, PUT, DELETE')
                self._write('Access-Control-Allow-Headers: %s\r\n' % 'Content-Type')
                # end of change
        else:
            self._write('Status: %s\r\n' % self.status)