# MailHog Email Testing Environment

This directory contains the Docker setup for MailHog, a testing SMTP server with a web interface for viewing emails.

## Features

- Captures all outgoing emails from the application
- Web interface to view emails
- SMTP server for testing email functionality
- No real emails are sent to actual recipients

## Usage

1. Start MailHog:
```bash
docker-compose up -d
```

2. Access the web interface:
- Open http://localhost:8025 in your browser
- All emails sent by the application will appear here

3. Configuration:
- SMTP Server: localhost
- SMTP Port: 1025
- No authentication required
- No TLS required

## Testing Email Verification

1. Register a new user in the application
2. Check the MailHog web interface (http://localhost:8025)
3. Find the verification email
4. Click the verification link or copy it to your browser

## Stopping MailHog

```bash
docker-compose down
```

To remove all data (including stored emails):
```bash
docker-compose down -v
``` 