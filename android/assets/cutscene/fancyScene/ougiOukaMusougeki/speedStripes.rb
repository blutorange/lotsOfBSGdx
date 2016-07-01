count = 2000
speed = 0.375
delay = 0.03
duration = 10.800
rays = ["OUGI_OUKA_MUSOUGEKI_RAY3","OUGI_OUKA_MUSOUGEKI_RAY14","OUGI_OUKA_MUSOUGEKI_RAY15"]

visibleDelay = 0
lastSpeed = 0
totalDelay = 0.0
numberOfStripes = nil
lastPositionY = 0

sprite = Array.new(count){|i|
    "Sprite R 0.0 S#{i.to_s.rjust(3,'0')} 100.0 #{rays.sample}"
}
position = Array.new(count){|i|
    positionY = Random.rand(-4.5..4.5)
    positionY = Random.rand(-4.5..4.5) while ((positionY-lastPositionY).abs<1)
    s = "Position R 0.0 S#{i.to_s.rjust(3,'0')} -10 #{positionY}"
    lastPositionY = positionY
    s
}
opacity = Array.new(count){|i|
    "Opacity A 0.0 S#{i.to_s.rjust(3,'0')} #{Random.rand(0.5..0.8)}"
}
move = Array.new(count){|i|
    d = Random.rand((0.5*delay)..(2*delay))
    s = Random.rand((0.8*speed)..(1.2*speed))
    lastSpeed = s
    totalDelay += d
    if totalDelay + s - d > duration && numberOfStripes == nil
          visibleDelay = totalDelay
          numberOfStripes = i+1
    end
    "Move R #{d} S#{i.to_s.rjust(3,'0')} Line #{s} R 20 #{Random.rand((-0.2)..(-0.1))}"    
}
show = Array.new(count){|i|
    "Show R 0.0 S#{i.to_s.rjust(3,'0')} #{lastSpeed+visibleDelay}"
}

puts sprite[0,numberOfStripes].join("\n")
puts ""
puts position[0,numberOfStripes].join("\n")
puts ""
puts opacity[0,numberOfStripes].join("\n")
puts ""
puts move[0,numberOfStripes].join("\n")
puts ""
puts show[0,numberOfStripes].join("\n")