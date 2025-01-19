package com.app.api.qnaboard.service;

import com.app.api.qnaboard.dto.QnaBoardDto;
import com.app.domain.member.entity.Member;
import com.app.domain.member.service.MemberService;
import com.app.domain.qnaboard.constant.ContentStatus;
import com.app.domain.qnaboard.entity.QnaBoard;
import com.app.domain.qnaboard.service.AttachmentService;
import com.app.domain.qnaboard.service.QnaBoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class QnaBoardInfoService {

    private final QnaBoardService qnaBoardService;
    private final MemberService memberService;
    private final AttachmentService attachmentService;

    @Transactional
    public Long createQnaBoard(QnaBoardDto.Request requestDto) {
        Member member = memberService.findMemberById(requestDto.getMemberId());

        /// 첨부파일 없이 QnaBoard 저장
        QnaBoard qnaBoard = QnaBoard.builder()
                .title(requestDto.getTitle())
                .content(requestDto.getContent())
                .attachments(new ArrayList<>())
                .member(member)
                .contentStatus(ContentStatus.ACTIVATE)
                .build();
        QnaBoard savedBoard = qnaBoardService.createQnaBoard(qnaBoard);

        if (requestDto.getFiles() != null && !requestDto.getFiles().isEmpty()) {
            attachmentService.saveAttachments(requestDto.getFiles(), savedBoard);
        }

        // QnaBoard 저장 및 ID 반환
        return qnaBoard.getQnaBoardId();
    }

    @Transactional(readOnly = true)
    public QnaBoardDto.Response getQnaBoardById(Long qnaBoardId) {
        QnaBoard qnaBoard = qnaBoardService.findQnaBoardByIdWithDetails(qnaBoardId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시글입니다."));

        return QnaBoardDto.Response.of(qnaBoard);
    }

    @Transactional(readOnly = true)
    public List<QnaBoardDto.Response> getAllQnaBoards() {
//        return qnaBoardDomainService.findQnaBoards()
//                .stream()
//                .map(QnaBoardDto.Response::of)
//                .collect();
        return null;
    }

    public QnaBoardDto.Response updateQnaBoard(Long qnaBoardId, QnaBoardDto.Request requestDto) {
        return null;
    }

    // 잘 처리됐다 라는 response를 돌려줘야 (공통 response작성)
    public void deleteQnaBoard(Long qnaBoardId) {
        // status처리
    }
}
