package com.lgcns.newspacebackend.domain.news.service;

import com.lgcns.newspacebackend.domain.news.dto.NewsCategoryRequestDto;
import com.lgcns.newspacebackend.domain.news.dto.NewsCategoryResponseDto;
import com.lgcns.newspacebackend.domain.news.entity.NewsCategory;
import com.lgcns.newspacebackend.domain.news.repository.NewsCategoryRepository;
import com.lgcns.newspacebackend.global.exception.BaseException;
import com.lgcns.newspacebackend.global.exception.BaseResponseStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class NewsCategoryService {

    private final NewsCategoryRepository newsCategoryRepository;

    // 카테고리 등록
    @Transactional
    public NewsCategoryResponseDto createNewsCategory(NewsCategoryRequestDto requestDto) {

        NewsCategory category = NewsCategory.builder()
                .name(requestDto.getName())
                .icon(requestDto.getIcon())
                .build();
        newsCategoryRepository.save(category);

        return new NewsCategoryResponseDto(category);
    }

    // 카테고리 목록 조회
    @Transactional(readOnly = true)
    public List<NewsCategoryResponseDto> getNewsCategories() {
        List<NewsCategoryResponseDto> categoryResponseDtoList = newsCategoryRepository.findAll()
                .stream()
                .map(NewsCategoryResponseDto::new)
                .collect(Collectors.toList());

        return categoryResponseDtoList;
    }

    // 카테고리 수정
    @Transactional
    public NewsCategoryResponseDto updateNewsCategory(Long categoryId, NewsCategoryRequestDto requestDto) {
        NewsCategory category = getNewsCategoryForRepository(categoryId);

        category.setName(requestDto.getName());
        category.setIcon(requestDto.getIcon());
        newsCategoryRepository.save(category);

        return new NewsCategoryResponseDto(category);
    }

    // 카테고리 삭제
    @Transactional
    public void deleteNewsCategory(Long categoryId) {
        NewsCategory category = getNewsCategoryForRepository(categoryId);

        newsCategoryRepository.delete(category);
    }

    // categoryId로 repository 에서 category 조회
    private NewsCategory getNewsCategoryForRepository(Long categoryId) {
        NewsCategory category = newsCategoryRepository.findById(categoryId)
                .orElseThrow(() -> new BaseException(BaseResponseStatus.NOT_FOUND_CATEGORY));
        return category;
    }
}
