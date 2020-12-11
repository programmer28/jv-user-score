package core.basesyntax;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Arrays;

public class UserServiceTest {
    private static final String[] basicRecords = {"john@gmail.com:78", "rick@yahoo.com:99",
            "greg@gmail.com:20", "russell@mail.us:141", "jerry@mail.us:0", "mortimer@mail.us:53",
            "test@gmail.com:2", "bob@mail.com:141986"};
    private static final String[] EMPTY_ARRAY = {};
    private static final String[] singleElementArray = {"carl@mail.com:30"};
    private static final String EXCEPTION_CLASS = "core.basesyntax.exception.UserNotFoundException";
    private static UserService userService;

    @BeforeClass
    public static void init() {
        userService = new UserService();
    }

    @Rule
    public ExpectedException expectedEx = ExpectedException.none();

    @Test
    public void getUserScore_validCase() {
        Assert.assertEquals("Wrong score for user with email mortimer@mail.us.",
                53, userService.getUserScore(basicRecords, "mortimer@mail.us"));
        Assert.assertEquals("Wrong score for user with email rick@yahoo.com",
                99, userService.getUserScore(basicRecords, "rick@yahoo.com"));
        Assert.assertEquals("Wrong score for user with email greg@gmail.com",
                20, userService.getUserScore(basicRecords, "greg@gmail.com"));
        Assert.assertEquals("Wrong score for user with email jerry@mail.us",
                0, userService.getUserScore(basicRecords, "jerry@mail.us"));
        Assert.assertEquals("Wrong score for user with email bob@mail.com",
                141986, userService.getUserScore(basicRecords, "bob@mail.com"));
        Assert.assertEquals("Wrong score for user with email test@gmail.com",
                2, userService.getUserScore(basicRecords, "test@gmail.com"));
    }

    @Test
    public void exceptionClassExists() {
        try {
            Class.forName(EXCEPTION_CLASS);
        } catch (ClassNotFoundException e) {
            Assert.fail("You should create class called 'UserNotFoundException' inside of " +
                    "'exception' package");
        }
    }

    @Test
    public void exceptionClassExtendsFromRuntime() {
        try {
            Class<?> runtimeClass = Class.forName(EXCEPTION_CLASS).getSuperclass();
            Assert.assertEquals("Your own exception should be unchecked",
                    runtimeClass, RuntimeException.class);
        } catch (ClassNotFoundException e) {
            Assert.fail("You should create class called 'UserNotFoundException' inside of " +
                    "'exception' package");
        }
    }

    @Test
    public void exceptionClassHasConstructorWithString() {
        try {
            boolean containsConstructorWithParameter = false;
            Class<?> customExceptionClass = Class.forName(EXCEPTION_CLASS);
            Constructor<?>[] constructors = customExceptionClass.getConstructors();
            for (Constructor constructor : constructors) {
                Class[] parameterTypes = constructor.getParameterTypes();
                if(containsConstructorWithParameter
                        = Arrays.asList(parameterTypes).contains(String.class)){
                    break;
                }
            }
            Assert.assertTrue("Don't hardcode the message in the exception class, " +
                    "let's pass it to constructor", containsConstructorWithParameter);
        } catch (ClassNotFoundException e) {
            Assert.fail("You should create class called 'UserNotFoundException' inside of " +
                    "'exception' package");
        }
    }

    @Test
    public void getUserScore_withoutThrowsException() {
        Class<? extends UserService> userServiceClass = userService.getClass();
        try {
            Method getUserScoreMethod = userServiceClass
                    .getDeclaredMethod("getUserScore", String[].class, String.class);
            Class<?>[] throwsExceptions = getUserScoreMethod.getExceptionTypes();
            Assert.assertEquals("Should method getUserScore() throw an exception in " +
                    "the method signature?", 0, throwsExceptions.length);
        } catch (NoSuchMethodException e) {
            Assert.fail("You should not change method signature getUserScore()");
        }
    }

    @Test
    public void getUserScore_exceptionExpected() {
        String email = "vincent@mail.us";
        try {
            Class<?> exceptionClass = Class
                    .forName(EXCEPTION_CLASS);
            expectedEx.expect((Class<? extends RuntimeException>) exceptionClass);
            expectedEx.expectMessage(String.format("User with given email doesn't exist"));
            expectedEx.reportMissingExceptionWithMessage(String.format("User with given email: " +
                    "%s doesn't exist in the records: %s", email, Arrays.toString(EMPTY_ARRAY)));
            userService.getUserScore(EMPTY_ARRAY, email);
        } catch (ClassNotFoundException e) {
            Assert.fail("Should create a class called 'UserNotFoundException'.");
        }
    }

    @Test
    public void getUserScore_wrongEmailFormatInputFirstCase() {
        String email = "carl@mail.com:30";
        try {
            Class<?> exceptionClass = Class
                    .forName(EXCEPTION_CLASS);
            expectedEx.expect((Class<? extends RuntimeException>) exceptionClass);
            expectedEx.expectMessage("User with given email doesn't exist");
            expectedEx.reportMissingExceptionWithMessage(String.format("You should throw an " +
                    "exception 'UserNotFoundException' with message " +
                    "'User with given email doesn't exist' for " +
                    "input email: %s and records: %s", email, Arrays.toString(singleElementArray)));
            userService.getUserScore(singleElementArray, email);
        } catch (ClassNotFoundException e) {
            Assert.fail("Should throw exception whenever user with given email doesn't exist");
        }
    }

    @Test
    public void getUserScore_wrongEmailFormatInputSecondCase() {
        String email = "jerry@mail.us:0$@";
        try {
            Class<?> exceptionClass = Class
                    .forName(EXCEPTION_CLASS);
            expectedEx.expect((Class<? extends RuntimeException>) exceptionClass);
            expectedEx.expectMessage("User with given email doesn't exist");
            expectedEx.reportMissingExceptionWithMessage(String.format("You should throw an " +
                    "exception 'UserNotFoundException' with message " +
                    "'User with given email doesn't exist' for " +
                    "input email: %s and record: %s", email, Arrays.toString(basicRecords)));
            userService.getUserScore(basicRecords, email);
        } catch (ClassNotFoundException e) {
            Assert.fail("Should throw exception whenever user with given email doesn't exist");
        }
    }

    @Test
    public void getUserScore_wrongEmailFormatInputThirdCase() {
        String email = "bob@mail.com:1419";
        try {
            Class<?> exceptionClass = Class
                    .forName(EXCEPTION_CLASS);
            expectedEx.expect((Class<? extends RuntimeException>) exceptionClass);
            expectedEx.expectMessage("User with given email doesn't exist");
            expectedEx.reportMissingExceptionWithMessage(String.format("You should throw an " +
                    "exception 'UserNotFoundException' with message " +
                    "'User with given email doesn't exist' for " +
                    "input email: %s and record: %s", email, Arrays.toString(basicRecords)));
            userService.getUserScore(basicRecords, email);
        } catch (ClassNotFoundException e) {
            Assert.fail("Should throw exception whenever user with given email doesn't exist");
        }
    }
}
