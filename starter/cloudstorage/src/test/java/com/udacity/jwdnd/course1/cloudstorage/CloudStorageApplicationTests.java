package com.udacity.jwdnd.course1.cloudstorage;

import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CloudStorageApplicationTests {

	@LocalServerPort
	private int port;

	private String baseURL;

	private WebDriver driver;

	@BeforeAll
	static void beforeAll() {
		WebDriverManager.chromedriver().setup();
	}

	@BeforeEach
	public void beforeEach() {
		this.driver = new ChromeDriver();
		this.baseURL = "http://localhost:" + this.port;
	}

	@AfterEach
	public void afterEach() {
		if (this.driver != null) {
			driver.quit();
		}
	}

	@Test
	public void getLoginPage() {
		driver.get("http://localhost:" + this.port + "/login");
		Assertions.assertEquals("Login", driver.getTitle());
	}

	@Test
	public void getSignupPage() {
		driver.get(baseURL+"/signup");
		Assertions.assertEquals("Sign Up", driver.getTitle());
	}

	@Test
	public void getHomePage() {
		driver.get(baseURL+"/home");
		Assertions.assertEquals("Login", driver.getTitle());
	}

	@Test
	public void testUserSignupLoginAndLogout() {
		String firstName = "Azeem";
		String lastName = "Michael";
		String username = "amichael";
		String password = "secret";

		driver.get(baseURL + "/signup");
		SignupPage signupPage = new SignupPage(driver);
		signupPage.signup(firstName, lastName, username, password);

		driver.get(baseURL+"/login");
		LoginPage loginPage = new LoginPage(driver);
		loginPage.login(username, password);

		Assertions.assertEquals(baseURL+"/home", driver.getCurrentUrl());
		Assertions.assertEquals("Home", driver.getTitle());

		driver.get(baseURL+"/home");
		HomePage homePage = new HomePage(driver);
		homePage.logout();

		Assertions.assertEquals("Login", driver.getTitle());
	}

	@Test
	public void testNotesCRUD() {
		this.authenticateTestUser();
		driver.get(baseURL+"/home");
		HomePage homePage = new HomePage(driver);

		try {
			// Create Note
			Note newNote = homePage.createNote("test", "this is a test");
			Assertions.assertEquals("test", newNote.getNoteTitle());
			Assertions.assertEquals("this is a test", newNote.getNoteDescription());

			// Update Note
			Note updatedNote = homePage.updateNote("foo", "bar");
			Assertions.assertEquals("foo", updatedNote.getNoteTitle());
			Assertions.assertEquals("bar", updatedNote.getNoteDescription());

			// Delete Note
			homePage.deleteNote();
			Assertions.assertTrue(homePage.isNoteDeleted());
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testCredentialsCRUD() {
		this.authenticateTestUser();
		driver.get(baseURL+"/home");
		HomePage homePage = new HomePage(driver);
		String url = "gmail.com";
		String username = "amichael";
		String password = "secret";

		try {
			// Create/Verify Credential
			homePage.createCredential(url, username, password);
			Assertions.assertTrue(homePage.isCredentialCreated(url, username));
			Assertions.assertTrue(homePage.isPasswordEncrypted(password));

			// Update/Verify Credential
			homePage.updateCredential("foo", "bar");
			Assertions.assertTrue(homePage.isCredentialUpdated("foo"));

			// Delete/Verify Credential
			homePage.deleteCredential();
			Assertions.assertTrue(homePage.isCredentialDeleted());

		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	private void authenticateTestUser() {
		driver.get(baseURL + "/signup");
		SignupPage signupPage = new SignupPage(driver);
		signupPage.signup("Azeem", "Michael", "amichael", "secret");

		driver.get(baseURL+"/login");
		LoginPage loginPage = new LoginPage(driver);
		loginPage.login("amichael", "secret");
	}

}
