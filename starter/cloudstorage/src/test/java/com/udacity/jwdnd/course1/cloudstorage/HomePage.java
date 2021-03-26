package com.udacity.jwdnd.course1.cloudstorage;

import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import org.openqa.selenium.*;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class HomePage {

    @FindBy(xpath="//button[contains(.,'Logout')]")
    private WebElement logoutButton;

    @FindBy(id = "nav-notes-tab")
    private WebElement navNotesTabButton;

    @FindBy(xpath = "//div[@id='nav-notes']//button[contains(.,'+ Add a New Note')]")
    private WebElement addNewNoteButton;

    @FindBy(id="noteModal")
    private WebElement noteModal;

    @FindBy(id="note-id")
    private WebElement noteId;

    @FindBy(id="note-title")
    private WebElement noteTitleField;

    @FindBy(id="note-description")
    private WebElement noteDescriptionField;

    @FindBy(id="userTable")
    private WebElement notesTable;

    @FindBy(id = "nav-credentials-tab")
    private WebElement navCredentialsTabButton;

    @FindBy(xpath = "//div[@id='nav-credentials']//button[contains(.,'+ Add a New Credential')]")
    private WebElement addNewCredentialButton;

    @FindBy(id = "credential-url")
    private WebElement credentialUrlField;

    @FindBy(id = "credential-username")
    private WebElement credentialUsernameField;

    @FindBy(id = "credential-password")
    private WebElement credentialPasswordField;

    @FindBy(id = "credentialModal")
    private WebElement credentialModal;

    @FindBy(id="credentialTable")
    private WebElement credentialsTable;

    public HomePage(WebDriver webDriver) {
        PageFactory.initElements(webDriver, this);
    }

    public void logout() {
        this.logoutButton.click();
    }

    public Note createNote(String noteTitle, String noteDescription) throws InterruptedException {
        this.navNotesTabButton.click();
        Thread.sleep(1000);

        this.addNewNoteButton.click();
        Thread.sleep(2000);

        this.noteTitleField.sendKeys(noteTitle);
        this.noteDescriptionField.sendKeys(noteDescription);
        Thread.sleep(1000);

        this.noteModal.findElement(By.xpath("//button[contains(.,'Save changes')]")).click();
        Thread.sleep(1000);

        this.navNotesTabButton.click();

        String title = this.notesTable.findElement(By.xpath("//tbody/tr[1]/th[1]")).getText();
        String description = this.notesTable.findElement(By.xpath("//tbody/tr[1]/td[2]")).getText();
        return new Note(title, description);
    }

    public Note updateNote(String newTitle, String newDescription) throws InterruptedException {
        this.navNotesTabButton.click();
        Thread.sleep(1000);

        this.notesTable.findElement(By.xpath("//button[contains(.,'Edit')]")).click();
        Thread.sleep(1000);

        this.noteTitleField.sendKeys(Keys.chord(Keys.COMMAND, "a"), newTitle);
        this.noteDescriptionField.sendKeys(Keys.chord(Keys.COMMAND, "a"), newDescription);
        Thread.sleep(1000);

        this.noteModal.findElement(By.xpath("//button[contains(.,'Save changes')]")).click();

        Thread.sleep(1000);
        this.navNotesTabButton.click();

        String title = this.notesTable.findElement(By.xpath("//tbody/tr[1]/th[1]")).getText();
        String description = this.notesTable.findElement(By.xpath("//tbody/tr[1]/td[2]")).getText();
        return new Note(title, description);
    }

    public void deleteNote() throws InterruptedException {
        this.navNotesTabButton.click();
        Thread.sleep(1000);
        this.notesTable.findElement(By.xpath("//tbody/tr[1]/td[1]//a[contains(.,'Delete')]")).click();
    }

    public Boolean isNoteDeleted() throws InterruptedException {
        Thread.sleep(1000);
        WebElement row = this.notesTable.findElement(By.tagName("tbody")).findElement(By.tagName("tr"));
        return row.findElements(By.tagName("td"))
                .get(0)
                .getText()
                .equals("No Notes to Show");
    }

    public void createCredential(String url, String username, String password) throws InterruptedException {
        this.navCredentialsTabButton.click();
        Thread.sleep(1000);

        this.addNewCredentialButton.click();
        Thread.sleep(1000);

        this.credentialUrlField.sendKeys(url);
        this.credentialUsernameField.sendKeys(username);
        this.credentialPasswordField.sendKeys(password+"\n");
        Thread.sleep(1000);
    }

    public Boolean isCredentialCreated(String url, String username) throws InterruptedException {
        this.navCredentialsTabButton.click();
        Thread.sleep(1000);
        String createdUrl = this.credentialsTable.findElement(By.xpath("//tbody/tr[1]/th[1]")).getText();
        String createdUsername = this.credentialsTable.findElement(By.xpath("//tbody/tr[1]/td[2]")).getText();
        return url.equals(createdUrl) && username.equals(createdUsername);
    }

    public Boolean isPasswordEncrypted(String password) throws InterruptedException {
        this.navCredentialsTabButton.click();
        Thread.sleep(1000);
        String createdPassword = this.credentialsTable.findElement(By.xpath("//tbody/tr[1]/td[3]")).getText();
        return !createdPassword.isBlank() && !createdPassword.equals(password);
    }

    public void updateCredential(String newUsernme, String newPassword) throws InterruptedException {
        this.navCredentialsTabButton.click();
        Thread.sleep(1000);
        this.credentialsTable.findElement(By.xpath("//button[contains(.,'Edit')]")).click();
        Thread.sleep(1000);

        this.credentialUsernameField.sendKeys(Keys.chord(Keys.COMMAND, "a"), newUsernme);
        this.credentialPasswordField.sendKeys(Keys.chord(Keys.COMMAND, "a"), newPassword+"\n");
        Thread.sleep(1000);
    }

    public Boolean isCredentialUpdated(String newUsername) throws InterruptedException {
        this.navCredentialsTabButton.click();
        Thread.sleep(1000);
        return this.credentialsTable.findElement(By.xpath("//tbody/tr[1]/td[2]")).getText().equals(newUsername);
    }

    public void deleteCredential() throws InterruptedException {
        this.navCredentialsTabButton.click();
        Thread.sleep(1000);
        this.credentialsTable.findElement(By.xpath("//tbody/tr[1]/td[1]//a[contains(.,'Delete')]")).click();
        Thread.sleep(1000);
    }

    public Boolean isCredentialDeleted() throws InterruptedException {
        Thread.sleep(1000);
        return this.credentialsTable
                .findElement(By.tagName("tbody"))
                .findElement(By.tagName("tr"))
                .findElements(By.tagName("td"))
                .get(0)
                .getText()
                .equals("No Credentials to Show");
    }
}
