package zen.proverbs.beans;

import com.fasterxml.jackson.annotation.JsonRootName;
import io.micronaut.context.annotation.Primary;
import io.micronaut.core.annotation.Introspected;

import java.util.List;

@Primary
@Introspected
@JsonRootName("zens")
public class ZenBeans {
    private List<ZenBean> zens;

    public List<ZenBean> getZens() {
        return zens;
    }

    public void setZens(List<ZenBean> zens) {
        this.zens = zens;
    }
}
