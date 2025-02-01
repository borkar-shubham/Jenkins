#!/bin/bash

# Check if the script is being run as root
if [ "$(id -u)" -ne 0 ]; then
  echo "This script must be run as root or with sudo."
  exit 1
fi

# Print the current date and time
echo "Current date and time: $(date)"

# Run a command that requires sudo (example: list all users)
echo "Listing all users with sudo..."
sudo cat /etc/passwd

# Run another simple command using sudo (e.g., check disk space)
echo "Disk space usage:"
sudo df -h

# Print a final message
echo "Script completed successfully."
