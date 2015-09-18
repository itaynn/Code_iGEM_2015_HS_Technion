How to use:

  Downloads Solr (preferably version 5.3.0 and up). You'll need Java 7 and above, too.

  Extract the compressed archive to the library of your choice, say: ROOT_PATH.
  
  open cmd (for instruction for Linux and Mac see Solr's documentation)
  
  run:
  
      cd ROOT_PATH
      
      bin\solr start -p 8983
      
      bin/solr create -c igem_core  -e techproducts
  
  
Copy the config files from this folder to ROOT_PATH\server\solr\igem_core\conf

  In order to index files, run in cmd:
      cd ROOT_PATH
      
      java -jar -Dc=igem_core example/exampledocs/post.jar ./PATH_TO_DATA/*
      
  The search engine will wait for you in http://localhost:8983/solr/igem_core/browse.
  
  
