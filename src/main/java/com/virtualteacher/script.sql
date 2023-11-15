create table lectures
(
    lecture_id  int auto_increment
        primary key,
    title       varchar(50)    not null,
    description varchar(1000)  null,
    video_url   varchar(255)   not null,
    assignment  varchar(10000) not null
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

create table courses
(
    course_id   int auto_increment
        primary key,
    title       varchar(50)   not null,
    topic_id    int           not null,
    description varchar(1000) null,
    start_date  datetime      not null,
    constraint courses_topics_topic_id_fk
        foreign key (topic_id) references topics (topic_id)
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

create table users
(
    user_id             int auto_increment
        primary key,
    first_name          varchar(255)   not null,
    last_name           varchar(255)   not null,
    email               varchar(255)   not null,
    password            varchar(255)   not null,
    role_id             int            not null,
    profile_picture_url varchar(10000) not null,
    constraint users_roles_role_id_fk
        foreign key (role_id) references roles (role_id)
);

create table course_comments
(
    comment_id int auto_increment
        primary key,
    user_id    int            not null,
    course_id  int            not null,
    content    varchar(10000) not null,
    constraint course_comments_courses_course_id_fk
        foreign key (course_id) references courses (course_id),
    constraint course_comments_users_user_id_fk
        foreign key (user_id) references users (user_id)
);

create table enrolled_courses
(
    user_id   int not null,
    course_id int not null,
    constraint enrolled_courses_courses_course_id_fk
        foreign key (course_id) references courses (course_id),
    constraint enrolled_courses_users_user_id_fk
        foreign key (user_id) references users (user_id)
);

