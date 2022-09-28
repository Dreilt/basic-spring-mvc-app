package pl.dreilt.basicspringmvcapp.validator;

import org.springframework.web.multipart.MultipartFile;
import pl.dreilt.basicspringmvcapp.annotation.ProfileImage;

import javax.imageio.ImageIO;
import javax.imageio.stream.ImageInputStream;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ProfileImageValidator implements ConstraintValidator<ProfileImage, MultipartFile> {

    @Override
    public void initialize(ProfileImage constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(MultipartFile profileImage, ConstraintValidatorContext context) {
        List<String> errorMessages = new ArrayList<>();
        String message;

        if (!profileImage.isEmpty()) {
            String[] allowedFileTypes = new String[] { "image/jpeg", "image/jpg", "image/png" };
            String fileType = profileImage.getContentType();
            boolean isFileTypeValid = Arrays.stream(allowedFileTypes).anyMatch(fileType::equals);

            if (!isFileTypeValid) {
                message = "{form.field.profileImage.error.invalidFileType.message}";
                errorMessages.add(message);
            }

            if (isFileTypeValid) {
                try {
                    ImageInputStream imageInputStream = ImageIO.createImageInputStream(profileImage.getInputStream());
                    BufferedImage bufferedImage = ImageIO.read(imageInputStream);
                    if (bufferedImage.getWidth() > 500 || bufferedImage.getHeight() > 500) {
                        message = "{form.field.profileImage.error.invalidProfileImageSize.message}";
                        errorMessages.add(message);
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }

            if (profileImage.getSize() > 2097152) {
                message = "{form.field.profileImage.error.invalidFileSize.message}";
                errorMessages.add(message);
            }

            if (errorMessages.size() > 0) {
                context.disableDefaultConstraintViolation();
                for (String errorMessage : errorMessages) {
                    context.buildConstraintViolationWithTemplate(errorMessage).addConstraintViolation();
                }

                return false;
            }
        }

        return true;
    }
}
