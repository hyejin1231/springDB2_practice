package hello.itemservice.repository.jdbctemplate;

import hello.itemservice.domain.Item;
import hello.itemservice.repository.ItemRepository;
import hello.itemservice.repository.ItemSearchCond;
import hello.itemservice.repository.ItemUpdateDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.util.List;
import java.util.Optional;

/**
 * JdbcTemplate
 */
@Slf4j
@Repository
public class JdbcTemplateItemRepositoryV1 implements ItemRepository {

    private final JdbcTemplate template;

    /**
     * dataSource를 의존관계 주입 받고 생성자 내부에서 jdbcTemplate를 생성한다.
     * @param dataSource
     */
    public JdbcTemplateItemRepositoryV1(DataSource dataSource) {
        this.template = new JdbcTemplate(dataSource);
    }

    @Override
    public Item save(Item item) {
        String sql = "insert into item(item_name, price, quantity) values (?,?,?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        return null;
    }

    @Override
    public void update(Long itemId, ItemUpdateDto updateParam) {

    }

    @Override
    public Optional<Item> findById(Long id) {
        return Optional.empty();
    }

    @Override
    public List<Item> findAll(ItemSearchCond cond) {
        return null;
    }
}
