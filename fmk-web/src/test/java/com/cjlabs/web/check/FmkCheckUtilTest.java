// package com.cjlabs.web.check;
//
// import com.cjlabs.core.types.longs.FmkUserId;
// import com.cjlabs.web.exception.Error200Exception;
// import com.cjlabs.web.exception.Error200ExceptionEnum;
// import com.cjlabs.web.exception.Error500Exception;
// import com.cjlabs.web.exception.Error500ExceptionEnum;
// import com.cjlabs.web.exception.Error400Exception;
// import com.cjlabs.web.exception.Error400ExceptionEnum;
// import com.cjlabs.web.threadlocal.FmkContextInfo;
// import com.cjlabs.web.threadlocal.FmkContextUtil;
// import org.junit.jupiter.api.AfterEach;
// import org.junit.jupiter.api.DisplayName;
// import org.junit.jupiter.api.Nested;
// import org.junit.jupiter.api.Test;
//
// import static org.junit.jupiter.api.Assertions.*;
//
// @DisplayName("FmkCheckUtil Tests")
// class FmkCheckUtilTest {
//
//     @AfterEach
//     void tearDown() {
//         // Clear context after each test to prevent test pollution
//         FmkContextUtil.clear();
//     }
//
//     @Nested
//     @DisplayName("Login Check Tests")
//     class LoginCheckTests {
//
//         @Test
//         @DisplayName("Should return user ID when user is logged in")
//         void shouldReturnUserIdWhenUserIsLoggedIn() {
//             // Arrange
//             FmkUserId expectedUserId = new FmkUserId(123L);
//             FmkContextInfo contextInfo = new FmkContextInfo();
//             contextInfo.setUserId(expectedUserId);
//             FmkContextUtil.setContextInfo(contextInfo);
//
//             // Act
//             FmkUserId actualUserId = FmkCheckUtil.checkLogin();
//
//             // Assert
//             assertEquals(expectedUserId, actualUserId);
//         }
//
//         @Test
//         @DisplayName("Should throw BusinessException when user is not logged in")
//         void shouldThrowBusinessExceptionWhenUserIsNotLoggedIn() {
//             // Arrange
//             FmkContextInfo contextInfo = new FmkContextInfo();
//             FmkContextUtil.setContextInfo(contextInfo);
//
//             // Act & Assert
//             Error200Exception exception = assertThrows(Error200Exception.class,
//                     FmkCheckUtil::checkLogin);
//             assertEquals("BUSINESS_ERROR", exception.getErrorType());
//             assertEquals(Error200ExceptionEnum.UNAUTHORIZED.getKey(), exception.getErrorKey());
//         }
//
//         @Test
//         @DisplayName("Should throw BusinessException when context is null")
//         void shouldThrowBusinessExceptionWhenContextIsNull() {
//             // Arrange - no context set
//
//             // Act & Assert
//             Error200Exception exception = assertThrows(Error200Exception.class,
//                     FmkCheckUtil::checkLogin);
//             assertEquals("BUSINESS_ERROR", exception.getErrorType());
//             assertEquals(Error200ExceptionEnum.UNAUTHORIZED.getKey(), exception.getErrorKey());
//         }
//     }
//
//     @Nested
//     @DisplayName("Input Validation Tests")
//     class InputValidationTests {
//
//         @Test
//         @DisplayName("checkInput(boolean) should throw ValidationException when condition is true")
//         void checkInputShouldThrowValidationExceptionWhenConditionIsTrue() {
//             // Act & Assert
//             Error400Exception exception = assertThrows(Error400Exception.class,
//                     () -> FmkCheckUtil.checkInput(true));
//             assertEquals("VALIDATION_ERROR", exception.getErrorType());
//             assertEquals(Error400ExceptionEnum.INVALID_PARAMETER.getKey(), exception.getErrorKey());
//         }
//
//         @Test
//         @DisplayName("checkInput(boolean) should not throw exception when condition is false")
//         void checkInputShouldNotThrowExceptionWhenConditionIsFalse() {
//             // Act & Assert
//             assertDoesNotThrow(() -> FmkCheckUtil.checkInput(false));
//         }
//
//         @Test
//         @DisplayName("checkInput(boolean, ValidationExceptionEnum) should throw specific exception when condition is true")
//         void checkInputWithEnumShouldThrowSpecificExceptionWhenConditionIsTrue() {
//             // Act & Assert
//             Error400Exception exception = assertThrows(Error400Exception.class,
//                     () -> FmkCheckUtil.throw400Error(true, Error400ExceptionEnum.INVALID_PARAMETER));
//             assertEquals("VALIDATION_ERROR", exception.getErrorType());
//             assertEquals(Error400ExceptionEnum.INVALID_PARAMETER.getKey(), exception.getErrorKey());
//         }
//
//         @Test
//         @DisplayName("checkInput(boolean, ValidationExceptionEnum) should not throw exception when condition is false")
//         void checkInputWithEnumShouldNotThrowExceptionWhenConditionIsFalse() {
//             // Act & Assert
//             assertDoesNotThrow(() -> FmkCheckUtil.throw400Error(false, Error400ExceptionEnum.INVALID_PARAMETER));
//         }
//     }
//
//     @Nested
//     @DisplayName("Data Validation Tests")
//     class DataValidationTests {
//
//         @Test
//         @DisplayName("checkData(boolean) should throw ValidationException when condition is true")
//         void checkDataShouldThrowValidationExceptionWhenConditionIsTrue() {
//             // Act & Assert
//             Error400Exception exception = assertThrows(Error400Exception.class,
//                     () -> FmkCheckUtil.checkInput(true));
//             assertEquals("VALIDATION_ERROR", exception.getErrorType());
//             assertEquals(Error400ExceptionEnum.INVALID_PARAMETER.getKey(), exception.getErrorKey());
//         }
//
//         @Test
//         @DisplayName("checkData(boolean) should not throw exception when condition is false")
//         void checkDataShouldNotThrowExceptionWhenConditionIsFalse() {
//             // Act & Assert
//             assertDoesNotThrow(() -> FmkCheckUtil.checkInput(false));
//         }
//
//         @Test
//         @DisplayName("checkData(boolean, ValidationExceptionEnum) should throw specific exception when condition is true")
//         void checkDataWithEnumShouldThrowSpecificExceptionWhenConditionIsTrue() {
//             // Act & Assert
//             Error400Exception exception = assertThrows(Error400Exception.class,
//                     () -> FmkCheckUtil.throw400Error(true, Error400ExceptionEnum.INVALID_PARAMETER));
//             assertEquals("VALIDATION_ERROR", exception.getErrorType());
//             assertEquals(Error400ExceptionEnum.INVALID_PARAMETER.getKey(), exception.getErrorKey());
//         }
//
//         @Test
//         @DisplayName("checkData(boolean, ValidationExceptionEnum) should not throw exception when condition is false")
//         void checkDataWithEnumShouldNotThrowExceptionWhenConditionIsFalse() {
//             // Act & Assert
//             assertDoesNotThrow(() -> FmkCheckUtil.throw400Error(false, Error400ExceptionEnum.INVALID_PARAMETER));
//         }
//     }
//
//     @Nested
//     @DisplayName("System Exception Tests")
//     class Error500ExceptionTests {
//
//         @Test
//         @DisplayName("throwSystem(SystemExceptionEnum) should throw specified exception")
//         void throwSystemShouldThrowSpecifiedException() {
//             // Act & Assert
//             Error500Exception exception = assertThrows(Error500Exception.class,
//                     () -> FmkCheckUtil.throw500Error(Error500ExceptionEnum.UNKNOWN_ERROR));
//             assertEquals("SYSTEM_ERROR", exception.getErrorType());
//             assertEquals(Error500ExceptionEnum.UNKNOWN_ERROR.getKey(), exception.getErrorKey());
//         }
//
//         @Test
//         @DisplayName("throwSystem(boolean, SystemExceptionEnum) should throw exception when condition is true")
//         void throwSystemWithConditionShouldThrowExceptionWhenConditionIsTrue() {
//             // Act & Assert
//             Error500Exception exception = assertThrows(Error500Exception.class,
//                     () -> FmkCheckUtil.throw500Error(true, Error500ExceptionEnum.UNKNOWN_ERROR));
//             assertEquals("SYSTEM_ERROR", exception.getErrorType());
//             assertEquals(Error500ExceptionEnum.UNKNOWN_ERROR.getKey(), exception.getErrorKey());
//         }
//
//         @Test
//         @DisplayName("throwSystem(boolean, SystemExceptionEnum) should not throw exception when condition is false")
//         void throwSystemWithConditionShouldNotThrowExceptionWhenConditionIsFalse() {
//             // Act & Assert
//             assertDoesNotThrow(() -> FmkCheckUtil.throw500Error(false, Error500ExceptionEnum.UNKNOWN_ERROR));
//         }
//     }
//
//     @Nested
//     @DisplayName("Business Exception Tests")
//     class Error200ExceptionTests {
//
//         @Test
//         @DisplayName("throwBusiness(BusinessExceptionEnum) should throw specified exception")
//         void throwBusinessShouldThrowSpecifiedException() {
//             // Act & Assert
//             Error200Exception exception = assertThrows(Error200Exception.class,
//                     () -> FmkCheckUtil.throw200Error(Error200ExceptionEnum.UNAUTHORIZED));
//             assertEquals("BUSINESS_ERROR", exception.getErrorType());
//             assertEquals(Error200ExceptionEnum.UNAUTHORIZED.getKey(), exception.getErrorKey());
//         }
//
//         @Test
//         @DisplayName("throwBusiness(boolean, BusinessExceptionEnum) should throw exception when condition is true")
//         void throwBusinessWithConditionShouldThrowExceptionWhenConditionIsTrue() {
//             // Act & Assert
//             Error200Exception exception = assertThrows(Error200Exception.class,
//                     () -> FmkCheckUtil.throw500Error(true, Error200ExceptionEnum.UNAUTHORIZED));
//             assertEquals("BUSINESS_ERROR", exception.getErrorType());
//             assertEquals(Error200ExceptionEnum.UNAUTHORIZED.getKey(), exception.getErrorKey());
//         }
//
//         @Test
//         @DisplayName("throwBusiness(boolean, BusinessExceptionEnum) should not throw exception when condition is false")
//         void throwBusinessWithConditionShouldNotThrowExceptionWhenConditionIsFalse() {
//             // Act & Assert
//             assertDoesNotThrow(() -> FmkCheckUtil.throw500Error(false, Error200ExceptionEnum.UNAUTHORIZED));
//         }
//     }
//
//     @Nested
//     @DisplayName("Validation Exception Tests")
//     class Error400ExceptionTests {
//
//         @Test
//         @DisplayName("throwValidation(ValidationExceptionEnum) should throw specified exception")
//         void throwValidationShouldThrowSpecifiedException() {
//             // Act & Assert
//             Error400Exception exception = assertThrows(Error400Exception.class,
//                     () -> FmkCheckUtil.throw400Error(Error400ExceptionEnum.INVALID_PARAMETER));
//             assertEquals("VALIDATION_ERROR", exception.getErrorType());
//             assertEquals(Error400ExceptionEnum.INVALID_PARAMETER.getKey(), exception.getErrorKey());
//         }
//
//         @Test
//         @DisplayName("throwValidation(boolean, ValidationExceptionEnum) should throw exception when condition is true")
//         void throwValidationWithConditionShouldThrowExceptionWhenConditionIsTrue() {
//             // Act & Assert
//             Error400Exception exception = assertThrows(Error400Exception.class,
//                     () -> FmkCheckUtil.throw400Error(true, Error400ExceptionEnum.INVALID_PARAMETER));
//             assertEquals("VALIDATION_ERROR", exception.getErrorType());
//             assertEquals(Error400ExceptionEnum.INVALID_PARAMETER.getKey(), exception.getErrorKey());
//         }
//
//         @Test
//         @DisplayName("throwValidation(boolean, ValidationExceptionEnum) should not throw exception when condition is false")
//         void throwValidationWithConditionShouldNotThrowExceptionWhenConditionIsFalse() {
//             // Act & Assert
//             assertDoesNotThrow(() -> FmkCheckUtil.throw400Error(false, Error400ExceptionEnum.INVALID_PARAMETER));
//         }
//     }
// }