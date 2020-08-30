drop schema if exists ins_octopus;
create schema ins_octopus collate utf8mb4_general_ci;

use  ins_octopus;

drop table if exists t_req_cfg;
/**请求配置*/
create table t_req_cfg
(
    id bigint auto_increment  primary key,
    uri varchar(255) not null ,
    param varchar(500) not null ,
    ins_company varchar(255) not null default 'PICC',
    method varchar(50) not null default 'GET',
    content_type varchar(50) not null default 'application/x-www-form-urlencoded',
    resp_param_names json   ,
    state varchar(25) not null default 'INIT',
    cookie varchar(200) not null,
    change_time datetime null,
    changer varchar(255) null,
    create_time datetime null,
    creator varchar(255) null,
    yn smallint(1) not null default 1
)comment '请求配置';


/**连续几年未出事故*/
drop table if exists t_data_no_dam;
create table t_data_no_dam
(
    id bigint auto_increment  primary key,
    req_id bigint not null ,
    years int not null ,
    context json  not null  comment '返回的参数',
    change_time datetime null,
    changer varchar(255) null,
    create_time datetime null,
    creator varchar(255) null,
    yn smallint(1) not null default 1
)COMMENT '连续几年未出事故';

/**去年保险信息*/
drop table if exists t_data_last_ins_info;
create table t_data_last_ins_info
(
    id bigint auto_increment  primary key,
    req_id bigint not null,
    owner_id_no varchar(255)  comment '车主身份证号',
    owner_addr varchar(255)  comment '车主地址',
    owner_name varchar(255)  comment '车主姓名',
    owner_mobile varchar(255)  comment '车主电话',

    car_brand varchar(255)  comment '车辆品牌',
    car_enroll_date varchar(50) comment '车辆初等日期',
    car_frame_no varchar(50) comment '车架号',
    car_license_no varchar(50) comment '车牌号',
    car_purchase_premium varchar(50) comment '新车购置价',
    ci_end_date varchar(50) comment '交强险到期日',
    bi_end_date varchar(50) comment '商业险到期日',

    context json  comment '返回的参数',
    change_time datetime null,
    changer varchar(255) null,
    create_time datetime null,
    creator varchar(255) null,
    yn smallint(1) not null default 1
)comment '去年保险信息';
/**去年险种费用*/

drop table if exists t_data_last_ins_kind_premium;
create table t_data_last_ins_kind_premium
(
    id bigint auto_increment  primary key,
    last_ins_info_id bigint not null ,
    kind_code varchar(255)  comment '险种代码',
    kind_name varchar(255)  comment '险种名称',
    premium varchar(255)  comment '险种费用',

    change_time datetime null,
    changer varchar(255) null,
    create_time datetime null,
    creator varchar(255) null,
    yn smallint(1) not null default 1
)COMMENT '去年保险险种费用';




select * from t_req_cfg;
update t_req_cfg set content_type='application/x-www-form-urlencoded';

insert into t_req_cfg(uri,param,method,cookie)
values ('/khyx/qth/price/quoteRenew.do','lastPolicyNo=PDAA201951010000521345&engineNo4Renew1=&frameNo4Renew1=065494&licenseNo4Renew=&licenseType4Renew=02&engineNo4Renew2=&frameNo4Renew2=&frameNo4Renew3=&isOwner=false&isQuoteRenewByLicenseNo=1'
,'GET','UE85OlEqAK14WIm1SiOkmyUrawv24M24xnfG5kZQgER6M68h5ZW0!-1124165325'
);

insert into t_req_cfg(uri,param,method,cookie)
values ('/khyx/qth/price/quotePolicy.do','policyNo=PDAA201951010000661078'
       ,'GET','UE85OlEqAK14WIm1SiOkmyUrawv24M24xnfG5kZQgER6M68h5ZW0!-1124165325'
       );