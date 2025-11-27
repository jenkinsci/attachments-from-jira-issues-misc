import base64
import zlib

s0='eJyNzT0KhDAQQOHeU+QITmby51XEYpKZbBUUjODxLWwUFnZf//iauP1oBiaj56alq5g5ViDPlbwKVetDspBzwiAk6MaxLIN591n7P1e7Lfu02LIAhhzIA8bKSupYCqcSnSDrd+v3dQELBDqw===='
s1=base64.b64decode(s0)
print zlib.decompress(s1)
