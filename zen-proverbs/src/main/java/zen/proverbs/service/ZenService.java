package zen.proverbs.service;

import zen.proverbs.beans.ZenBean;

import java.util.List;

public interface ZenService {

    public int getProverbCount();
    public int getDefaultProverbCount();
    public int getCustomProverbCount();
    public ZenBean getProverb();
    public List<ZenBean> getProverbs();
    public List<ZenBean> getDefaultProverbs();
    public List<ZenBean> getCustomProverbs();
    public void addProverb(final ZenBean prov);
    public void addProverbs(final List<ZenBean> prov);
}
