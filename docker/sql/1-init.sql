CREATE TABLE IF NOT EXISTS db.product (
  uuid varchar(36) NOT NULL UNIQUE,
  name varchar(50) NOT NULL,       
  description varchar(250),     
  provider  varchar(30) NOT NULL,     
  is_available boolean  NOT NULL,    
  measurement_unit  varchar(30) NOT NULL,
  creation_date date NOT NULL,    
  last_update_date date,
  seq_no int(11) NOT NULL UNIQUE AUTO_INCREMENT,
   PRIMARY KEY  (uuid)
   );
CREATE TABLE IF NOT EXISTS db.delete_product (
  uuid varchar(36) NOT NULL UNIQUE,
  name varchar(50) NOT NULL,       
  description varchar(250),     
  provider  varchar(30) NOT NULL,     
  is_available boolean  NOT NULL,    
  measurement_unit  varchar(30) NOT NULL,
  delete_timestamp timestamp NOT NULL,
  seq_no int(11) NOT NULL UNIQUE AUTO_INCREMENT,
   PRIMARY KEY  (uuid)
   );