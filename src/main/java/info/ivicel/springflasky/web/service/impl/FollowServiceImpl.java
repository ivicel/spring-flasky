package info.ivicel.springflasky.web.service.impl;

import info.ivicel.springflasky.web.model.domain.User;
import info.ivicel.springflasky.web.repository.FollowRepository;
import info.ivicel.springflasky.web.service.FollowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service("followService")
public class FollowServiceImpl implements FollowService {

    private FollowRepository followRepository;

    @Autowired
    public FollowServiceImpl(FollowRepository followRepository) {
        this.followRepository = followRepository;
    }

    @Override
    public <T> Page<T> findAllByFollower(User user, Class<T> type, Pageable pageable) {
        return followRepository.findAllByFollower(user, type, pageable);
    }

    @Override
    public <T> Page<T> findAllByFollowed(User user, Class<T> type, Pageable pageable) {
        return followRepository.findAllByFollowed(user, type, pageable);
    }
}
