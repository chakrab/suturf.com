package zen.proverbs.beans;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonRootName;
import io.micronaut.context.annotation.Primary;
import io.micronaut.core.annotation.Introspected;

@Primary
@Introspected
@JsonRootName("zen")
@JsonIgnoreProperties( { "type" })
public class ZenBean {
    private char   type;
    private String author;
    private String proverb;

    public ZenBean() {

    }

    public ZenBean(final char type, final String author, final String proverb) {
        this.type = type;
        this.author = author;
        this.proverb = proverb;
    }

    public char getType() {
        return type;
    }

    public void setType(char type) {
        this.type = type;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getProverb() {
        return proverb;
    }

    public void setProverb(String proverb) {
        this.proverb = proverb;
    }
}
