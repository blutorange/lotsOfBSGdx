count = 2000
durationSingle = 2.000
delay = 0.01
duration = 3.0
fadeTime = 0.1
spawnHeight = 1.5..11.5
sakura = "OUGI_OUKA_MUSOUGEKI_SAKURA"

visibleDelay = 0
totalDelay = 0.0
numberOfStripes = nil
lastPositionY = 0

animation = Array.new(count){|i|
    "Animation A 0.000 S#{i.to_s.rjust(3,'0')} 32.0 #{sakura}"
}
initial = Array.new(count){|i|
    positionY = Random.rand(spawnHeight)
    positionY = Random.rand(spawnHeight) while ((positionY-lastPositionY).abs<1)
    s = "Position R 0.0 S#{i.to_s.rjust(3,'0')} 5.0 #{positionY.round(3).to_s.rjust(3,?0)}
Opacity A 0.0 S#{i.to_s.rjust(3,'0')} 0.0"
    lastPositionY = positionY
    s
}
move = Array.new(count){|i|
    d = Random.rand((0.5*delay)..(2*delay))
    s = Random.rand((0.8*durationSingle)..(1.2*durationSingle))
    totalDelay += d
    if totalDelay + s - d > duration && numberOfStripes == nil
          visibleDelay = totalDelay
          numberOfStripes = i+1
    end
    "Move A #{(totalDelay-d).round(3).to_s.rjust(3,?0)} S#{i.to_s.rjust(3,'0')} Parabola #{s.round(3).to_s.rjust(3,?0)} =pow2In= R #{Random.rand(0.4..0.6).round(3).to_s.rjust(3,?0)} #{Random.rand(-4.5..-3.5).round(3).to_s.rjust(3,?0)} #{Random.rand(-7.5..-6.5).round(3).to_s.rjust(3,?0)} -10 -11
Fade A #{(totalDelay-d).round(3).to_s.rjust(3,?0)} S#{i.to_s.rjust(3,'0')} #{fadeTime.round(3).to_s.rjust(3,?0)} 1
Fade A #{(totalDelay-d+s-fadeTime).round(3).to_s.rjust(3,?0)} S#{i.to_s.rjust(3,'0')} #{fadeTime.round(3).to_s.rjust(3,?0)} 0
Show A #{(totalDelay-d).round(3).to_s.rjust(3,?0)} S#{i.to_s.rjust(3,'0')} #{s.round(3).to_s.rjust(3,?0)}"    
}

puts animation[0,numberOfStripes].join("\n")
puts ""
puts initial[0,numberOfStripes].join("\n")
puts ""
puts move[0,numberOfStripes].join("\n")