package ru.practicum.shareit.request.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.request.model.ItemRequest;

import java.util.List;

public interface ItemRequestRepository extends JpaRepository<ItemRequest, Long> {
    @Query("select i from ItemRequest i where not i.requestor.id=?1")
    List<ItemRequest> findAllIdByOtherId(Long id, Pageable pageable);

    @Query("select i from ItemRequest i where i.requestor.id=?1")
    List<ItemRequest> findAllIdByRequestorId(Long id, Pageable pageable);
}