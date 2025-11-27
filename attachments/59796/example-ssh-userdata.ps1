<powershell>

# Userdata script to enable SSH access as user Administrator via SSH keypair.
# This assumes that
# 1. the SSH service (sshd) has already been installed, configured, and started during AMI creation;
# 2. a valid SSH key is selected when the EC2 instance is being launched; and
# 3. IMDSv2 is selected when launching the EC2 instance.

# Save the private key from instance metadata.
$ImdsToken = (Invoke-WebRequest -Uri 'http://169.254.169.254/latest/api/token' -Method 'PUT' -Headers @{'X-aws-ec2-metadata-token-ttl-seconds' = 2160} -UseBasicParsing).Content
$ImdsHeaders = @{'X-aws-ec2-metadata-token' = $ImdsToken}
$AuthorizedKey = (Invoke-WebRequest -Uri 'http://169.254.169.254/latest/meta-data/public-keys/0/openssh-key' -Headers $ImdsHeaders -UseBasicParsing).Content
$AuthorizedKeysPath = 'C:\ProgramData\ssh\administrators_authorized_keys'
New-Item -Path $AuthorizedKeysPath -ItemType File -Value $AuthorizedKey -Force

# Set appropriate permissions on administrators_authorized_keys by copying them from an existing key.
Get-ACL C:\ProgramData\ssh\ssh_host_dsa_key | Set-ACL $AuthorizedKeysPath

# Ensure the SSH agent pulls in the new key.
Set-Service -Name ssh-agent -StartupType "Automatic"
Restart-Service -Name ssh-agent

</powershell>
