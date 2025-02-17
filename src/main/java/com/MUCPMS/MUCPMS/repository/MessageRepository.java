package com.MUCPMS.MUCPMS.repository;

import com.MUCPMS.MUCPMS.model.Message;
import com.MUCPMS.MUCPMS.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {

    List<Message> findBySenderAndReceiverOrderByTimestampAsc(User sender, User receiver);

}
