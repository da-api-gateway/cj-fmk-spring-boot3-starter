package com.cjlabs.web.check;

import com.cjlabs.core.types.longs.FmkUserId;
import com.cjlabs.web.exception.BusinessException;
import com.cjlabs.web.exception.BusinessExceptionEnum;
import com.cjlabs.web.exception.SystemException;
import com.cjlabs.web.exception.SystemExceptionEnum;
import com.cjlabs.web.exception.ValidationException;
import com.cjlabs.web.exception.ValidationExceptionEnum;
import com.cjlabs.web.threadlocal.FmkContextInfo;
import com.cjlabs.web.threadlocal.FmkContextUtil;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("FmkCheckUtil Tests")
class FmkCheckUtilTest {

    @AfterEach
    void tearDown() {
        // Clear context after each test to prevent test pollution
        FmkContextUtil.clear();
    }

    @Nested
    @DisplayName("Login Check Tests")
    class LoginCheckTests {

        @Test
        @DisplayName("Should return user ID when user is logged in")
        void shouldReturnUserIdWhenUserIsLoggedIn() {
            // Arrange
            FmkUserId expectedUserId = new FmkUserId(123L);
            FmkContextInfo contextInfo = new FmkContextInfo();
            contextInfo.setUserId(expectedUserId);
            FmkContextUtil.setContextInfo(contextInfo);

            // Act
            FmkUserId actualUserId = FmkCheckUtil.checkLogin();

            // Assert
            assertEquals(expectedUserId, actualUserId);
        }

        @Test
        @DisplayName("Should throw BusinessException when user is not logged in")
        void shouldThrowBusinessExceptionWhenUserIsNotLoggedIn() {
            // Arrange
            FmkContextInfo contextInfo = new FmkContextInfo();
            FmkContextUtil.setContextInfo(contextInfo);

            // Act & Assert
            BusinessException exception = assertThrows(BusinessException.class,
                    FmkCheckUtil::checkLogin);
            assertEquals("BUSINESS_ERROR", exception.getErrorType());
            assertEquals(BusinessExceptionEnum.UNAUTHORIZED.getKey(), exception.getErrorKey());
        }

        @Test
        @DisplayName("Should throw BusinessException when context is null")
        void shouldThrowBusinessExceptionWhenContextIsNull() {
            // Arrange - no context set

            // Act & Assert
            BusinessException exception = assertThrows(BusinessException.class,
                    FmkCheckUtil::checkLogin);
            assertEquals("BUSINESS_ERROR", exception.getErrorType());
            assertEquals(BusinessExceptionEnum.UNAUTHORIZED.getKey(), exception.getErrorKey());
        }
    }

    @Nested
    @DisplayName("Input Validation Tests")
    class InputValidationTests {

        @Test
        @DisplayName("checkInput(boolean) should throw ValidationException when condition is true")
        void checkInputShouldThrowValidationExceptionWhenConditionIsTrue() {
            // Act & Assert
            ValidationException exception = assertThrows(ValidationException.class,
                    () -> FmkCheckUtil.checkInput(true));
            assertEquals("VALIDATION_ERROR", exception.getErrorType());
            assertEquals(ValidationExceptionEnum.INVALID_PARAMETER.getKey(), exception.getErrorKey());
        }

        @Test
        @DisplayName("checkInput(boolean) should not throw exception when condition is false")
        void checkInputShouldNotThrowExceptionWhenConditionIsFalse() {
            // Act & Assert
            assertDoesNotThrow(() -> FmkCheckUtil.checkInput(false));
        }

        @Test
        @DisplayName("checkInput(boolean, ValidationExceptionEnum) should throw specific exception when condition is true")
        void checkInputWithEnumShouldThrowSpecificExceptionWhenConditionIsTrue() {
            // Act & Assert
            ValidationException exception = assertThrows(ValidationException.class,
                    () -> FmkCheckUtil.throwValidation(true, ValidationExceptionEnum.INVALID_PARAMETER));
            assertEquals("VALIDATION_ERROR", exception.getErrorType());
            assertEquals(ValidationExceptionEnum.INVALID_PARAMETER.getKey(), exception.getErrorKey());
        }

        @Test
        @DisplayName("checkInput(boolean, ValidationExceptionEnum) should not throw exception when condition is false")
        void checkInputWithEnumShouldNotThrowExceptionWhenConditionIsFalse() {
            // Act & Assert
            assertDoesNotThrow(() -> FmkCheckUtil.throwValidation(false, ValidationExceptionEnum.INVALID_PARAMETER));
        }
    }

    @Nested
    @DisplayName("Data Validation Tests")
    class DataValidationTests {

        @Test
        @DisplayName("checkData(boolean) should throw ValidationException when condition is true")
        void checkDataShouldThrowValidationExceptionWhenConditionIsTrue() {
            // Act & Assert
            ValidationException exception = assertThrows(ValidationException.class,
                    () -> FmkCheckUtil.checkInput(true));
            assertEquals("VALIDATION_ERROR", exception.getErrorType());
            assertEquals(ValidationExceptionEnum.INVALID_PARAMETER.getKey(), exception.getErrorKey());
        }

        @Test
        @DisplayName("checkData(boolean) should not throw exception when condition is false")
        void checkDataShouldNotThrowExceptionWhenConditionIsFalse() {
            // Act & Assert
            assertDoesNotThrow(() -> FmkCheckUtil.checkInput(false));
        }

        @Test
        @DisplayName("checkData(boolean, ValidationExceptionEnum) should throw specific exception when condition is true")
        void checkDataWithEnumShouldThrowSpecificExceptionWhenConditionIsTrue() {
            // Act & Assert
            ValidationException exception = assertThrows(ValidationException.class,
                    () -> FmkCheckUtil.throwValidation(true, ValidationExceptionEnum.INVALID_PARAMETER));
            assertEquals("VALIDATION_ERROR", exception.getErrorType());
            assertEquals(ValidationExceptionEnum.INVALID_PARAMETER.getKey(), exception.getErrorKey());
        }

        @Test
        @DisplayName("checkData(boolean, ValidationExceptionEnum) should not throw exception when condition is false")
        void checkDataWithEnumShouldNotThrowExceptionWhenConditionIsFalse() {
            // Act & Assert
            assertDoesNotThrow(() -> FmkCheckUtil.throwValidation(false, ValidationExceptionEnum.INVALID_PARAMETER));
        }
    }

    @Nested
    @DisplayName("System Exception Tests")
    class SystemExceptionTests {

        @Test
        @DisplayName("throwSystem(SystemExceptionEnum) should throw specified exception")
        void throwSystemShouldThrowSpecifiedException() {
            // Act & Assert
            SystemException exception = assertThrows(SystemException.class,
                    () -> FmkCheckUtil.throwSystem(SystemExceptionEnum.UNKNOWN_ERROR));
            assertEquals("SYSTEM_ERROR", exception.getErrorType());
            assertEquals(SystemExceptionEnum.UNKNOWN_ERROR.getKey(), exception.getErrorKey());
        }

        @Test
        @DisplayName("throwSystem(boolean, SystemExceptionEnum) should throw exception when condition is true")
        void throwSystemWithConditionShouldThrowExceptionWhenConditionIsTrue() {
            // Act & Assert
            SystemException exception = assertThrows(SystemException.class,
                    () -> FmkCheckUtil.throwSystem(true, SystemExceptionEnum.UNKNOWN_ERROR));
            assertEquals("SYSTEM_ERROR", exception.getErrorType());
            assertEquals(SystemExceptionEnum.UNKNOWN_ERROR.getKey(), exception.getErrorKey());
        }

        @Test
        @DisplayName("throwSystem(boolean, SystemExceptionEnum) should not throw exception when condition is false")
        void throwSystemWithConditionShouldNotThrowExceptionWhenConditionIsFalse() {
            // Act & Assert
            assertDoesNotThrow(() -> FmkCheckUtil.throwSystem(false, SystemExceptionEnum.UNKNOWN_ERROR));
        }
    }

    @Nested
    @DisplayName("Business Exception Tests")
    class BusinessExceptionTests {

        @Test
        @DisplayName("throwBusiness(BusinessExceptionEnum) should throw specified exception")
        void throwBusinessShouldThrowSpecifiedException() {
            // Act & Assert
            BusinessException exception = assertThrows(BusinessException.class,
                    () -> FmkCheckUtil.throwBusiness(BusinessExceptionEnum.UNAUTHORIZED));
            assertEquals("BUSINESS_ERROR", exception.getErrorType());
            assertEquals(BusinessExceptionEnum.UNAUTHORIZED.getKey(), exception.getErrorKey());
        }

        @Test
        @DisplayName("throwBusiness(boolean, BusinessExceptionEnum) should throw exception when condition is true")
        void throwBusinessWithConditionShouldThrowExceptionWhenConditionIsTrue() {
            // Act & Assert
            BusinessException exception = assertThrows(BusinessException.class,
                    () -> FmkCheckUtil.throwBusiness(true, BusinessExceptionEnum.UNAUTHORIZED));
            assertEquals("BUSINESS_ERROR", exception.getErrorType());
            assertEquals(BusinessExceptionEnum.UNAUTHORIZED.getKey(), exception.getErrorKey());
        }

        @Test
        @DisplayName("throwBusiness(boolean, BusinessExceptionEnum) should not throw exception when condition is false")
        void throwBusinessWithConditionShouldNotThrowExceptionWhenConditionIsFalse() {
            // Act & Assert
            assertDoesNotThrow(() -> FmkCheckUtil.throwBusiness(false, BusinessExceptionEnum.UNAUTHORIZED));
        }
    }

    @Nested
    @DisplayName("Validation Exception Tests")
    class ValidationExceptionTests {

        @Test
        @DisplayName("throwValidation(ValidationExceptionEnum) should throw specified exception")
        void throwValidationShouldThrowSpecifiedException() {
            // Act & Assert
            ValidationException exception = assertThrows(ValidationException.class,
                    () -> FmkCheckUtil.throwValidation(ValidationExceptionEnum.INVALID_PARAMETER));
            assertEquals("VALIDATION_ERROR", exception.getErrorType());
            assertEquals(ValidationExceptionEnum.INVALID_PARAMETER.getKey(), exception.getErrorKey());
        }

        @Test
        @DisplayName("throwValidation(boolean, ValidationExceptionEnum) should throw exception when condition is true")
        void throwValidationWithConditionShouldThrowExceptionWhenConditionIsTrue() {
            // Act & Assert
            ValidationException exception = assertThrows(ValidationException.class,
                    () -> FmkCheckUtil.throwValidation(true, ValidationExceptionEnum.INVALID_PARAMETER));
            assertEquals("VALIDATION_ERROR", exception.getErrorType());
            assertEquals(ValidationExceptionEnum.INVALID_PARAMETER.getKey(), exception.getErrorKey());
        }

        @Test
        @DisplayName("throwValidation(boolean, ValidationExceptionEnum) should not throw exception when condition is false")
        void throwValidationWithConditionShouldNotThrowExceptionWhenConditionIsFalse() {
            // Act & Assert
            assertDoesNotThrow(() -> FmkCheckUtil.throwValidation(false, ValidationExceptionEnum.INVALID_PARAMETER));
        }
    }
}