package series.users.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import series.users.models.Permissions;
import series.users.models.SharedSerie;

import java.util.List;

@Service
public class SharedSeriesService implements ISharedSeriesService{

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    @Cacheable("shares")
    public List<SharedSerie> getAll() {
        String req = "SELECT * FROM SHARED";
        return jdbcTemplate.query(req, new BeanPropertyRowMapper(SharedSerie.class));
    }

    @Override
    @CacheEvict(cacheNames = {"shares"})
    public void shareSerie(Long userid, Long serieId, String permission) {
        String test = "INSERT INTO SHARED (USERID, SERIEID, PERMISSION) " +
        "SELECT " + userid + "," + serieId + ",'"+ Permissions.valueOf(permission) + "' " +
        "WHERE NOT EXISTS (SELECT 1 FROM SHARED  WHERE USERID="+userid+" AND SERIEID="+serieId+")";
        jdbcTemplate.execute(test);
    }

    @Override
    public void removeAccess(Long userid, Long serieid) {
        String req = "DELETE FROM SHARED WHERE USERID=" + userid + " AND SERIEID=" + serieid;
        jdbcTemplate.execute(req);
    }

    @Override
    public void privatizeSerie(Long serieId) {
        String req = "DELETE FROM SHARED WHERE SERIEID="+serieId;
        jdbcTemplate.execute(req);
    }

    @Override
    @Cacheable("myseries")
    public List<SharedSerie> sharedWithUser(Long userId) {
        String req = "SELECT * FROM SHARED WHERE USERID="+userId;
        return jdbcTemplate.query(req, new BeanPropertyRowMapper(SharedSerie.class));
    }
}
