package hello.hello_spring.service;

import hello.hello_spring.domain.Member;
import hello.hello_spring.repository.MemberRepository;
import hello.hello_spring.repository.MemoryMemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

//@Service
@Transactional //서비스 계층에 트랜잭션 추가 -> JPA 를 통한 모든 데이터 변경은 트랜잭션 안에서 실행해야 한다.
public class MemberService {

    // 기존에는 회원 서비스가 메모리 회원 리포지토리를 직접 생성하게 했다. DI 위반
    // private final MemberRepository memberRepository = new MemoryMemberRepository();

    // 회원 리포지토리의 코드가 회원 서비스 코드를 DI(의존성 주입) 가능하게 변경
    private final MemberRepository memberRepository;

    //생성자에 @Autowired 를 사용하면 객체 생성 시점에 스프링 컨테이너에서 해당 스프링 빈을 찾아서 주입한다.
    //생성자가 1개만 있으면 @Autowired 는 생략 할 수 있다.
//    @Autowired
    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    /*
     * 회원가입
     */
    public Long join(Member member) {

        validateDuplicateMember(member); //중복 회원 검증
        memberRepository.save(member);
        return member.getId();
    }

    private void validateDuplicateMember(Member member) {
        memberRepository.findByName(member.getName())
                .ifPresent(m -> {
                    throw new IllegalStateException("이미 존재하는 회원입니다.");
                });
    }

    /*
     * 전체 회원 조회
     */
    public List<Member> findMembers() {
        return memberRepository.findAll();
    }

    public Optional<Member> findOne(Long memberId) {
        return memberRepository.findById(memberId);
    }
}
