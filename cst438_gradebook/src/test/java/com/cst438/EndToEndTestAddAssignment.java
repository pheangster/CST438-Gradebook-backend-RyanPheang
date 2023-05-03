package com.cst438;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.springframework.beans.factory.annotation.Autowired;

import com.cst438.domain.Assignment;
import com.cst438.domain.AssignmentGradeRepository;
import com.cst438.domain.AssignmentRepository;
import com.cst438.domain.CourseRepository;
 
@SpringBootTest
public class EndToEndTestAddAssignment {
	public static final String CHROME_DRIVER_FILE_LOCATION = "Users/phea/Desktop/chromedriver";
	
	public static final String URL = "http://localhost:3000";
	public static final String TEST_USER_EMAIL = "test0@csumb.edu";
	public static final String TEST_INSTRUCTOR_EMAIL = "dwisneski@csumb.edu";
	public static final int SLEEP_DURATION = 1000; // 1 second.
	public static final String TEST_ASSIGNMENT_NAME = "Test Assignment34";
	public static final String TEST_COURSE_TITLE = "Test Course34";
	public static final String TEST_STUDENT_NAME = "Test34";
	public static final int TEST_COURSE_ID = 56789;
	public static final String TEST_DUE_DATE = "2023-05-05";
 
	@Autowired
	CourseRepository courseRepository;

	@Autowired
	AssignmentGradeRepository assignnmentGradeRepository;

	@Autowired
	AssignmentRepository assignmentRepository;

	@Test
	public void newAssignmentTest() throws Exception {
		// deletes existing assignments in the db
		Assignment x = null;
		do {			

			// get assignments in repo
            for (Assignment a : assignmentRepository.findAll()) {
            	System.out.println(a.getName()); // for debug
            	// check if assignment exists
            	if (a.getName().equals(TEST_ASSIGNMENT_NAME)) {
        			x = a;
        			break;
        		}
            }

            // if exists, delete to make test repeatable
            if (x != null)
                assignmentRepository.delete(x);

        } while (x != null);
        System.setProperty("webdriver.chrome.driver", CHROME_DRIVER_FILE_LOCATION);
        WebDriver driver = new ChromeDriver();
        // Puts an Implicit wait for 10 sec
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

		try {
			// load the page for the url (front end app)
	        driver.get(URL);
			Thread.sleep(SLEEP_DURATION);

			// find and press add assignment button
			driver.findElement(By.id("AddAssignment")).click();
			Thread.sleep(SLEEP_DURATION);

			// user inputs name, due date, and course id of assignment
			driver.findElement(By.id("assignmentName")).sendKeys(TEST_ASSIGNMENT_NAME);
			driver.findElement(By.id("dueDate")).sendKeys(TEST_DUE_DATE);
			driver.findElement(By.id("courseId")).sendKeys(Integer.toString(TEST_COURSE_ID));

			// find and press submit button
			driver.findElement(By.xpath("//button[@id='Submit']")).click();
			Thread.sleep(SLEEP_DURATION);

			// verify toast
			String toast_text = driver.findElement(By.cssSelector(".Toastify__toast-body div:nth-child(2)")).getText();
			Thread.sleep(SLEEP_DURATION);

			assertEquals(toast_text, "Assignment successfully added");

			// verify assignment is in database
			boolean found = false;
			for (Assignment a : assignmentRepository.findAll()) {
				System.out.println(a.getName()); // for debug
				// check if assignment exists
				if (a.getName().equals(TEST_ASSIGNMENT_NAME)) {
			        found = true;
			        break;
			    }
			}

			assertTrue(found, "Assignment was not added.");

		} catch (Exception ex) {

			throw ex;

		} finally { // deletes any existing assignments in the db 

			Assignment assignment = null;

			// find assignments in repository
			for (Assignment a : assignmentRepository.findAll()) {
				System.out.println(a.getName()); // for debug
				// check if assignment exists
            	if (a.getName().equals(TEST_ASSIGNMENT_NAME)) {
            		assignment = a;
        			break;
        		}
            }

			// if exists, delete to make test repeatable
            if (assignment != null)
                assignmentRepository.delete(assignment);

            driver.quit();
		}
	}
}
