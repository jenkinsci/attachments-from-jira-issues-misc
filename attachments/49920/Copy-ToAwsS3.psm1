<#
.SYNOPSIS
Uploads a given item to a specified AWS S3 bucket.

.DESCRIPTION
Uploads a file, all files in a directory (without scanning for sub-directories) or all files in a directory and all of its subdirectories to an AWS S3 bucket.
If uploading all files in a directory including its sub-directories the directory structure is maintained in the AWS S3 bucket.

This function is reliant on the AWS PowerShell Toolkit being installed on the system.

.PARAMETER ItemToUpload
The file or directory to be uploaded. The item must exist in the specified location on the system this function is run from.

.PARAMETER S3Bucket
The name of the AWS S3 bucket that the routing rule is to be applied to.

.PARAMETER S3RootFolder
A folder within the S3 bucket to upload to. If not specified the item will be uploaded to the root of the S3 bucket.

.PARAMETER S3AccessKey
The access key used for programmatically configuring AWS S3.

.PARAMETER S3SecretAccessKey
The secret access key used for programatically configuring AWS S3.

.PARAMETER TagSet
An array of zero or more Amazon.S3.Model.Tag objects that will be applied to all uploaded files.

.PARAMETER Recursive
Whether to upload everything in the specified location, including files in sub-directories.
If not specified and the item being uploaded is a directory, then only the files in that directory (and no lower) will be uploaded.
If specified and item being uploaded is a file this switch has no effect.

.PARAMETER Params
Any additional options to apply to the uploaded item in AWS S3 (e.g. whether to allow everyone to download the uploaded item).

.EXAMPLE
C:\PS> Upload-ToAwsS3 -ItemToUpload "C:\Content\Document.docx" -S3Bucket download.example.com -S3AccessKey INSERT_ACCESS_KEY -S3SecretAccessKey INSERT_SECRET_ACCESS_KEY -Params @{"CannedACLName" = "public-read"}

This would upload C:\Content\Document.docx to the root (top) level of the download.example.com AWS S3 bucket and would allow anyone over the internet to download the file.

.EXAMPLE
C:\PS> Upload-ToAwsS3 -ItemToUpload "C:\Content\Document.docx" -S3Bucket download.example.com -S3RootFolder PublicDocuments -S3AccessKey INSERT_ACCESS_KEY -S3SecretAccessKey INSERT_SECRET_ACCESS_KEY -Params @{"CannedACLName" = "public-read"}

This would upload C:\Content\Document.docx to the PublicDocuments folder inside the download.example.com AWS S3 bucket and would allow anyone over the internet to download the file.

.EXAMPLE
C:\PS> Upload-ToAwsS3 -ItemToUpload "C:\Content" -S3Bucket download.example.com -S3RootFolder PublicDocuments -S3AccessKey INSERT_ACCESS_KEY -S3SecretAccessKey INSERT_SECRET_ACCESS_KEY -Params @{"CannedACLName" = "public-read"}

This would upload all files inside C:\Content to the PublicDocuments folder inside the download.example.com AWS S3 bucket and would allow anyone over the internet to download any of these files. Any file contained in a sub-directory in C:\Contents would not be uploaded.

.EXAMPLE
C:\PS> Upload-ToAwsS3 -ItemToUpload "C:\Content" -S3Bucket download.example.com -S3RootFolder PublicDocuments -S3AccessKey INSERT_ACCESS_KEY -S3SecretAccessKey INSERT_SECRET_ACCESS_KEY -Recursive -Params @{"CannedACLName" = "public-read"}

This would upload all files inside C:\Content, including files in sub-directories to the PublicDocuments folder inside the download.example.com AWS S3 bucket and would allow anyone over the internet to download any of these files. The directory structure as contained in C:\Contents would be preserved in the uploaded location.
For example if uploading C:\Content recursively with the following structure:
C:\Content\Document.docx
C:\Content\Calculations.xlsx
C:\Content\Widgets\Widget1.png
C:\Content\Widgets\Widget2.png
C:\Content\Resources\default_skin.css
C:\Content\Resources\images\next.png

The PublicDocuments folder inside the download.example.com S3 bucket would consist of:
Document.docx
Calculations.xlsx
Widgets\Widget1.png
Widgets\Widget2.png
Resources\default_skin.css
Resources\images\next.png

.EXAMPLE
C:\PS> $tag1 = New-Object -TypeName Amazon.S3.Model.Tag
C:\PS> $tag1.Key = 'Configuration'
C:\PS> $tag1.Value = 'production'
C:\PS> $tag2 = New-Object -TypeName Amazon.S3.Model.Tag
C:\PS> $tag2.Key = 'Cause'
C:\PS> $tag2.Value = 'hotfix'
C:\PS> $tagSet = @($tag1,$tag2)
C:\PS> Upload-ToAwsS3 -ItemToUpload "C:\Content" -S3Bucket download.example.com -S3RootFolder PublicDocuments -S3AccessKey INSERT_ACCESS_KEY -S3SecretAccessKey INSERT_SECRET_ACCESS_KEY -Params @{"CannedACLName" = "public-read"}

This would upload all files inside C:\Content to the PublicDocuments folder inside the download.example.com AWS S3 bucket and would allow anyone over the internet to download any of these files. Any file contained in a sub-directory in C:\Contents would not be uploaded. All uploaded files will have the two tags applied to them.
#>
Function Upload-ToAwsS3
{
    [CmdletBinding()]
    Param
    (
        [Parameter(Mandatory=$true)]
        [ValidateScript({Test-Path $_})]
        [ValidateScript({![string]::IsNullOrWhiteSpace($_)})]
        [string]$ItemToUpload,

        [Parameter(Mandatory=$true)]
        [ValidateScript({![string]::IsNullOrWhiteSpace($_)})]
        [string]$S3Bucket,
        
        [Parameter(Mandatory=$false)]
        [string]$S3RootFolder,
        
        [Parameter(Mandatory=$true)]
        [ValidateScript({![string]::IsNullOrWhiteSpace($_)})]
        [string]$S3AccessKey,
        
        [Parameter(Mandatory=$true)]
        [ValidateScript({![string]::IsNullOrWhiteSpace($_)})]
        [string]$S3SecretAccessKey,
        
        [Parameter(Mandatory=$false)]
        [object[]]$TagSet = @(),

        [Parameter(Mandatory=$false)]
        [switch]$Recursive,
        
        [Parameter(Mandatory=$true)]
        $Params
    )

    # Check if our TagSet contains anything
    if ($TagSet.Count -gt 0)
    {
        Write-Verbose "Given Tag Set, to be applied to all uploaded objects:"
        Write-Verbose ($TagSet | Out-String)
    }

    # Gets all files and child folders within the given directory
    Get-ChildItem $ItemToUpload | %{
        $uploadTo = "{0}{1}" -f $S3RootFolder, $_.Name
        $uploadFrom = $_.FullName

        # Checks if the item is a folder.
        # Note that $ItemToUpload has already been verified as existing on the local file system, thus this check just validates whether the given path is a folder.
        if (Test-Path $uploadFrom -PathType Container)
        {
            Write-Verbose "Uploading directory $uploadFrom..." -Verbose:$Verbose

            # Inserts all files within a folder to AWS
            Write-Verbose "Recursive switch set to $Recursive" -Verbose:$Verbose

            if ($Recursive)
            {
                Write-S3Object -AccessKey $S3AccessKey -SecretKey $S3SecretAccessKey -BucketName $S3Bucket -KeyPrefix $uploadTo -Folder $uploadFrom -TagSet $TagSet -Recurse @Params -Verbose:$Verbose
            }
            else
            {
                Write-S3Object -AccessKey $S3AccessKey -SecretKey $S3SecretAccessKey -BucketName $S3Bucket -KeyPrefix $uploadTo -Folder $uploadFrom @Params -TagSet $TagSet -Verbose:$Verbose
            }
        }
        else
        {
            Write-Verbose "Uploading file $uploadFrom..." -Verbose:$Verbose

            # Inserts file to AWS
            Write-S3Object -AccessKey $S3AccessKey -SecretKey $S3SecretAccessKey -BucketName $S3Bucket -Key $uploadTo -File $uploadFrom -TagSet $TagSet @Params -Verbose:$Verbose
        }
    }
}

 <#
.SYNOPSIS
Copies the contents from a supplied local system location into a specified AWS S3 bucket.

.DESCRIPTION
Copies the contents from a supplied local system location into a specified AWS S3 bucket. The local system location could be either a single file or a directory.
Where the local system location is a directory and the Recurse switch is specified then all files in the directory including files in sub-directories will be uploaded with the directory structure preserved.
Otherwise if the local system location is a directory and the Recurse switch is not specified then only the files contained in that directory (and not the files contained in sub-directories) will be uploaded.

This function is reliant on the AWS PowerShell Toolkit being installed on the system.

.PARAMETER S3Region
The ID of the AWS region the S3 bucket resides in.

.PARAMETER SourceLocation
The file or directory to be uploaded. The item must exist in the specified location on the system this function is run from.

.PARAMETER S3Bucket
The name of the AWS S3 bucket that the routing rule is to be applied to.

.PARAMETER S3RootFolder
A folder within the S3 bucket to upload to. If not specified the item will be uploaded to the root of the S3 bucket.

.PARAMETER S3AccessKey
The access key used for programmatically configuring AWS S3.

.PARAMETER S3SecretAccessKey
The secret access key used for programatically configuring AWS S3.

.PARAMETER MakePublic
Whether the uploaded content should be made avaiable for anyone to download.

.PARAMETER TagSet
An array of zero or more Amazon.S3.Model.Tag objects that will be applied to all uploaded files.

.PARAMETER Recursive
Whether to upload everything in the specified location, including files in sub-directories.
If not specified and the item being uploaded is a directory, then only the files in that directory (and no lower) will be uploaded.
If specified and item being uploaded is a file this switch has no effect.

.EXAMPLE
C:\PS> Copy-ToAwsS3 -S3Region us-east-1 -SourceLocation "C:\Content\Document.docx" -S3Bucket download.example.com -S3AccessKey INSERT_ACCESS_KEY -S3SecretAccessKey INSERT_SECRET_ACCESS_KEY -MakePublic

This would upload C:\Content\Document.docx to the root (top) level of the download.example.com AWS S3 bucket hosted in the North Virginia data center and would allow anyone over the internet to download the file.

.EXAMPLE
C:\PS> Copy-ToAwsS3 -S3Region us-east-1 -SourceLocation "C:\Content\Document.docx" -S3Bucket download.example.com -S3RootFolder PublicDocuments -S3AccessKey INSERT_ACCESS_KEY -S3SecretAccessKey INSERT_SECRET_ACCESS_KEY -MakePublic

This would upload C:\Content\Document.docx to the PublicDocuments folder inside the download.example.com AWS S3 bucket that is hosted in the North Virginia data center and would allow anyone over the internet to download the file.

.EXAMPLE
C:\PS> Copy-ToAwsS3 -S3Region us-east-1 -SourceLocation "C:\Content\" -S3Bucket download.example.com -S3RootFolder PublicDocuments -S3AccessKey INSERT_ACCESS_KEY -S3SecretAccessKey INSERT_SECRET_ACCESS_KEY -MakePublic

This would upload all files inside C:\Content to the PublicDocuments folder inside the download.example.com AWS S3 bucket that is hosted in the North Virginia data center and would allow anyone over the internet to download any of these files. Any file contained in a sub-directory in C:\Contents would not be uploaded.

.EXAMPLE
C:\PS> Copy-ToAwsS3 -S3Region us-east-1 -SourceLocation "C:\Content\" -S3Bucket download.example.com -S3RootFolder PublicDocuments -S3AccessKey INSERT_ACCESS_KEY -S3SecretAccessKey INSERT_SECRET_ACCESS_KEY -MakePublic -Recursive

This would upload all files inside C:\Content, including files in sub-directories to the PublicDocuments folder inside the download.example.com AWS S3 bucket that is hosted in the North Virginia data center and would allow anyone over the internet to download any of these files. The directory structure as contained in C:\Contents would be preserved in the uploaded location.
For example if uploading C:\Content recursively with the following structure:
C:\Content\Document.docx
C:\Content\Calculations.xlsx
C:\Content\Widgets\Widget1.png
C:\Content\Widgets\Widget2.png
C:\Content\Resources\default_skin.css
C:\Content\Resources\images\next.png

The PublicDocuments folder inside the download.example.com S3 bucket would consist of:
Document.docx
Calculations.xlsx
Widgets\Widget1.png
Widgets\Widget2.png
Resources\default_skin.css
Resources\images\next.png

.EXAMPLE
C:\PS> $tag1 = New-Object -TypeName Amazon.S3.Model.Tag
C:\PS> $tag1.Key = 'Configuration'
C:\PS> $tag1.Value = 'production'
C:\PS> $tag2 = New-Object -TypeName Amazon.S3.Model.Tag
C:\PS> $tag2.Key = 'Cause'
C:\PS> $tag2.Value = 'hotfix'
C:\PS> $tagSet = @($tag1,$tag2)
C:\PS> Copy-ToAwsS3 -S3Region us-east-1 -SourceLocation "C:\Content\" -S3Bucket download.example.com -S3RootFolder PublicDocuments -S3AccessKey INSERT_ACCESS_KEY -S3SecretAccessKey INSERT_SECRET_ACCESS_KEY -TagSet $tagSet -MakePublic

This would upload all files inside C:\Content to the PublicDocuments folder inside the download.example.com AWS S3 bucket that is hosted in the North Virginia data center and would allow anyone over the internet to download any of these files. Any file contained in a sub-directory in C:\Contents would not be uploaded. All uploaded files will have the two tags applied to them.
#>
Function Copy-ToAwsS3
{
    [CmdletBinding()]
    Param
    (
        [Parameter(Mandatory=$true)]
        [ValidateScript({![string]::IsNullOrWhiteSpace($_)})]
        [string]$S3Region,

        [Parameter(Mandatory=$true)]
        [ValidateScript({Test-Path $_})]
        [ValidateScript({![string]::IsNullOrWhiteSpace($_)})]
        [string]$SourceLocation,

        [Parameter(Mandatory=$true)]
        [ValidateScript({![string]::IsNullOrWhiteSpace($_)})]
        [string]$S3Bucket,

        [Parameter(Mandatory=$false)]
        [string]$S3RootFolder,

        [Parameter(Mandatory=$false)]
        [ValidateScript({
            if ($_.Count -gt 0) {
                $true
            } else {
                throw "Given TagSet is empty"
            }
        })]
        [ValidateScript({
            $passed = $true
            $_ | ForEach-Object {
                if ($_.ToString() -ne 'Amazon.S3.Model.Tag')
                {
                    Write-Error "The following item in the given TagSet is not an Amazon.S3.Model.Tag"
                    $_ | Out-Host
                    $passed = $false
                }
            }
            $passed
        })]
        [object[]]$TagSet = @(),

        [Parameter(Mandatory=$true)]
        [ValidateScript({![string]::IsNullOrWhiteSpace($_)})]
        [string]$S3AccessKey,

        [Parameter(Mandatory=$true)]
        [ValidateScript({![string]::IsNullOrWhiteSpace($_)})]
        [string]$s3SecretAccesskey,

        [Parameter(Mandatory=$false)]
        [switch]$MakePublic,

        [Parameter(Mandatory=$false)]
        [switch]$Recursive
    )
    # Private Variables
    $params = @{}

    if ($MakePublic)
    {
        $params.add("CannedACLName", "public-read")
    }

    # If supplied format the value of $S3RootFolder appropriately
    if (!([string]::IsNullOrEmpty($S3RootFolder)) -and ($S3RootFolder[$S3RootFolder.Length - 1] -ne "/"))
    {
        $S3RootFolder = "{0}/" -f $S3RootFolder
    }

    # Initialises the Default AWS Region based on the region provided
    Set-DefaultAWSRegion -Region $S3Region -Verbose:$Verbose

    # Perform the upload
    Upload-ToAwsS3 -ItemToUpload $SourceLocation -S3AccessKey $S3AccessKey -S3SecretAccessKey $S3SecretAccessKey -S3Bucket $S3Bucket -S3RootFolder $S3RootFolder -TagSet $TagSet -Recursive:$Recursive -Params $params -Verbose:$Verbose
}

# Export the entrypoint function
Export-ModuleMember -Function "Copy-*"