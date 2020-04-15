
C:\Users\chao>F:

F:\>cd F:\学习资料\minio

F:\学习资料\minio>.\minio.exe server  E:\file(E:\file 为文件存储位置)

set MINIO_ACCESS_KEY=091QSF56YVRTK86NR8ZM
set MINIO_SECRET_KEY=QYB2i2VqmQR8Dp8y+ul+sRjUyQo6GahztRfTogQp


minio.exe server http://172.16.0.122/D:/minio/file1 http://172.16.0.122/D:/minio/file2 http://172.16.0.68/D:/minio/file http://172.16.0.68/D:/minio/file2

minio.exe server http://172.16.0.140/D:/minio/file http://172.16.0.140/D:/minio/file2 http://172.16.0.68/D:/minio/file http://172.16.0.68/D:/minio/file2


1.至少需要4个节点

2.需要暴露9000端口

3.需要依次执行上面脚本命令