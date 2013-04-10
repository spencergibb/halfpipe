package halfpipe.validation;

import com.google.common.base.Preconditions;
import halfpipe.util.Generics;
import org.springframework.core.io.AbstractResource;

import javax.validation.ConstraintValidator;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Set;

/**
 * User: spencergibb
 * Date: 4/10/13
 * Time: 7:16 AM
 */
public class ConstraintMappingResource extends AbstractResource {
    private static final String XML_HEADER = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n";
    private static final String HEADER = "<constraint-mappings xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://jboss.org/xml/ns/javax/validation/mapping validation-mapping-1.0.xsd\" xmlns=\"http://jboss.org/xml/ns/javax/validation/mapping\">\n";
    private static final String TEMPLATE = "    <constraint-definition annotation=\"%s\"><validated-by include-existing-validators=\"true\"><value>%s</value></validated-by></constraint-definition>\n";
    private static final String FOOTER = "</constraint-mappings>";

    final Set<ConstraintValidator> validators;

    public ConstraintMappingResource(Set<ConstraintValidator> validators) {
        Preconditions.checkNotNull(validators);
        this.validators = validators;
    }

    @Override
    public String getDescription() {
        return "constraint mapping ["+ validators.size() +"]";
    }

    @Override
    public InputStream getInputStream() throws IOException {
        StringBuilder out = new StringBuilder(XML_HEADER);
        out.append(HEADER);

        for (ConstraintValidator validator: validators) {
            Class<? extends ConstraintValidator> validatorClass = validator.getClass();
            Class<?> annotationClass = Generics.getTypeParameter(validatorClass);
            if (annotationClass == null) {
                System.err.println("ignoring ConstraintValidator "+validatorClass+" could not determine annotation class"); //TODO: Log warning
                continue;
            }
            String constraint = String.format(TEMPLATE, annotationClass.getName(), validatorClass.getName());
            out.append(constraint);
        }

        out.append(FOOTER);
        return new ByteArrayInputStream(out.toString().getBytes());
    }
}
