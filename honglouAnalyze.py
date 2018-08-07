
file = open("/home/yang/下载/红楼梦/all.txt")
str1 = file.read()
file.close()




def filterX(str):
	s = ""
	for e in str:
		if isChineseCharacter(e):
			s += e
	return s

def isChineseCharacter(e):
	x = ord(e)
	return x >= 0x4e00 and x <= 0x9fa5


pureC = filterX(str1)
print(pureC[:30])

x = pureC.find("第八十一回")


part1 = pureC[:x]
part2 = pureC[x:]

def count(str, obj):
	num = 0
	while(True):
		x = str.find(obj)
		if(x != -1):
			num += 1
			str = str[x + 1 :]
		else:
			break
	return num

baoyu1 = count(part1, "宝玉")
baoyu2 = count(part2, "宝玉")

kuangqie1 = count(part1, "况且")
kuangqie2 = count(part2, "况且")

zhidao1 = count(part1, "知道")
zhidao2 = count(part2, "知道")

print("宝玉 showed %d times in part1 with %d characters"%(baoyu1, len(part1)))
print("宝玉 showed %d times in part2"%baoyu2)

print("况且 showed %d times in part1 with %d characters"%(kuangqie1, len(part1)))
print("the ratio is %d"%(kuangqie1 // len(part1)))

print("况且 showed %d times in part2 with %d characters"%(kuangqie2, len(part2)))
print("the ratio is %d"%(kuangqie1 // len(part1)))

print("知道 showed %d times in part2 with %d characters"%(zhidao2, len(part2)))
print("the ratio is %d"%(zhidao2 // len(part2)))

print("知道 showed %d times in part1 with %d characters"%(zhidao1, len(part1)))
print("the ratio is %d"%(zhidao1 // len(part1)))

print("字数比是 %r"%(len(part2) / len(part1)))



