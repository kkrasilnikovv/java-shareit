package ru.practicum.shareit.item.comment;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static ru.practicum.shareit.item.ItemTestData.comment;
import static ru.practicum.shareit.item.ItemTestData.Item1;
import static ru.practicum.shareit.user.UserTestData.User1;

@DataJpaTest
public class CommentRepositoryTest {
    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private UserRepository userRepository;
    @Test
    @DirtiesContext
    void testFindByOwner() {
        userRepository.save(User1);
        itemRepository.save(Item1);
        commentRepository.save(comment);
        List<Comment> comments = commentRepository.findAllByItem(Item1);
        assertThat(comments.size(), equalTo(1));
    }
}
