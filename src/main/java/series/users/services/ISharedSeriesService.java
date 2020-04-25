package series.users.services;


import series.users.models.SharedSerie;

import java.util.List;

public interface ISharedSeriesService {
    public List<SharedSerie> getAll();
    public List<SharedSerie> sharedWithUser(Long userid);
    public void shareSerie(Long userid, Long serieId, String premission);
    public void removeAccess(Long userid, Long serieId);
    public void privatizeSerie(Long serieId);
}
