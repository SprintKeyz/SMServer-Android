Usage:

1. Set up SMServer on your jailbroken device. If it's crashing (it did for me on Taurine), disable SSL in SMServer settings.

2. Port forward your SMServer instance as external port 80 and set it as a static IP.

3. Use DuckDNS to map your external IP to a URL

4. Configure strings.xml and MyWebViewClient class to use the correct variables, such as external and internal hostname, SSID, etc.

5. Optional: use something like DuckDNS updater for windows on a separate computer to keep external IP up to date

6. Build and install app :)