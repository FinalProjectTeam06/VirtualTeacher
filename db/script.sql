


create table grades
(
    grade_id   int auto_increment
        primary key,
    grade_name varchar(100) not null
);

create table roles
(
    role_id int auto_increment
        primary key,
    name    varchar(255) not null
);

create table topics
(
    topic_id int auto_increment
        primary key,
    name     varchar(255) not null
);

create table users
(
    user_id             int auto_increment
        primary key,
    first_name          varchar(255)         not null,
    last_name           varchar(255)         not null,
    email               varchar(255)         not null,
    password            varchar(255)         not null,
    role_id             int                  not null,
    profile_picture_url varchar(10000)       not null,
    is_activated        tinyint(1) default 0 not null,
    constraint users_roles_role_id_fk
        foreign key (role_id) references roles (role_id)
);

create table courses
(
    course_id     int auto_increment
        primary key,
    title         varchar(50)          not null,
    topic_id      int                  not null,
    description   varchar(1000)        null,
    creator_id    int                  not null,
    is_published  tinyint(1) default 0 not null,
    start_date    datetime             not null,
    minimum_grade int                  not null,
    rating        double     default 5 not null,
    constraint courses_topics_topic_id_fk
        foreign key (topic_id) references topics (topic_id),
    constraint courses_users_user_id_fk
        foreign key (creator_id) references users (user_id)
);

create table enrolled_courses
(
    enrollment_id     int auto_increment
        primary key,
    user_id           int                  not null,
    course_id         int                  not null,
    isFinished        tinyint(1) default 0 not null,
    graduation_status tinyint(1) default 0 not null,
    constraint enrolled_courses_courses_course_id_fk
        foreign key (course_id) references courses (course_id),
    constraint enrolled_courses_users_user_id_fk
        foreign key (user_id) references users (user_id)
);

create table lectures
(
    lecture_id     int auto_increment
        primary key,
    title          varchar(50)    not null,
    description    varchar(1000)  null,
    video_url      varchar(255)   not null,
    assignment_url varchar(10000) not null,
    teacher_id     int            not null,
    course_id      int            not null,
    constraint lectures_courses_course_id_fk
        foreign key (course_id) references courses (course_id),
    constraint lectures_users_user_id_fk
        foreign key (teacher_id) references users (user_id)
);

create table assignments_submissions
(
    assignment_id  int auto_increment
        primary key,
    lecture_id     int            not null,
    user_id        int            not null,
    assignment_url varchar(10000) not null,
    grade_id       int default 1  null,
    constraint assignments_submissions_grades_grade_id_fk
        foreign key (grade_id) references grades (grade_id),
    constraint assignments_submissions_lectures_lecture_id_fk
        foreign key (lecture_id) references lectures (lecture_id),
    constraint assignments_submissions_users_user_id_fk
        foreign key (user_id) references users (user_id)
);

create table comments
(
    comment_id int auto_increment
        primary key,
    user_id    int           not null,
    lecture_id int           not null,
    content    varchar(1000) not null,
    constraint comments_lectures_lecture_id_fk
        foreign key (lecture_id) references lectures (lecture_id),
    constraint comments_users_user_id_fk
        foreign key (user_id) references users (user_id)
);

create table course_lectures
(
    course_id  int not null,
    lecture_id int not null,
    constraint course_lectures_courses_course_id_fk
        foreign key (course_id) references courses (course_id),
    constraint course_lectures_lectures_lecture_id_fk
        foreign key (lecture_id) references lectures (lecture_id)
);

create table notes
(
    note_id    int auto_increment
        primary key,
    user_id    int           not null,
    lecture_id int           not null,
    text       varchar(1000) null,
    constraint notes_lectures_lecture_id_fk
        foreign key (lecture_id) references lectures (lecture_id),
    constraint notes_users_user_id_fk
        foreign key (user_id) references users (user_id)
);

create table rates
(
    rate_id    int auto_increment
        primary key,
    user_id    int         not null,
    course_id  int         not null,
    rate_value int         null,
    comment    varchar(75) not null,
    constraint rates_courses_course_id_fk
        foreign key (course_id) references courses (course_id),
    constraint rates_users_user_id_fk
        foreign key (user_id) references users (user_id)
);

